package com.agenda.aplicativodelojavirturalcommercadopago.activities.activities.Pagamento;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.agenda.aplicativodelojavirturalcommercadopago.activities.interfaceMercadoPago.ComunicacaoServidorMP;
import com.agenda.aplicativodelojavirturalcommercadopago.activities.model.DB;
import com.agenda.aplicativodelojavirturalcommercadopago.databinding.ActivityPagamentoBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mercadopago.android.px.configuration.AdvancedConfiguration;
import com.mercadopago.android.px.core.MercadoPagoCheckout;
import com.mercadopago.android.px.model.Payment;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Pagamento extends AppCompatActivity {

    ActivityPagamentoBinding binding;
    private String tamanhoCalcado;
    private String nome;
    private String preco;

    private final String PUBLIC_KEY = "TEST-bf9c6031-a8b9-45f1-b8ab-bc44f3a3f691";
    private final String ACCESS_TOKEN = "TEST-247071788151941-111618-2df12070d747c1bd33d141b4e3b2098c-237998836";

    DB db = new DB();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPagamentoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tamanhoCalcado = Objects.requireNonNull(getIntent().getExtras()).getString("tamanho_calcado");
        nome = getIntent().getExtras().getString("nome");
        preco = getIntent().getExtras().getString("preco");


        binding.btFazerPagamento.setOnClickListener(v -> {

            String bairro = binding.editBairro.getText().toString();
            String rua_numero = binding.editRua.getText().toString();
            String cidade_estado = binding.editCidade.getText().toString();
            String celular = binding.editCelular.getText().toString();

            if (bairro.isEmpty() || rua_numero.isEmpty() || cidade_estado.isEmpty() || celular.isEmpty()){
                Snackbar snackbar = Snackbar.make(v,"Preencha todos os campos!",Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.RED);
                snackbar.setTextColor(Color.WHITE);
                snackbar.show();
            }else {
                criarJsonObject();
            }
        });
    }

    private void criarJsonObject(){

        JsonObject dados = new JsonObject();

        //Primeiro Item
        JsonArray item_lista = new JsonArray();
        JsonObject item;

        //Segundo Item
        JsonObject email = new JsonObject();

        //Terceiro Item - Excluir formas de pagamento - nesse caso vai ser o boleto

        item = new JsonObject();
        item.addProperty("title",nome);
        item.addProperty("quantity",1);
        item.addProperty("currency_id","BRL");
        item.addProperty("unit_price",Double.parseDouble(preco));
        item_lista.add(item);

        dados.add("items",item_lista);

        String emailUsuario = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        email.addProperty("email",emailUsuario);
        dados.add("payer",email);

        Log.d("j",dados.toString());
        criarPreferenciaPagamento(dados);
    }

    private void criarPreferenciaPagamento(JsonObject dados){

        String site = "https://api.mercadopago.com";
        String url = "/checkout/preferences?access_token=" + ACCESS_TOKEN;

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(site)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ComunicacaoServidorMP conexao_pagamento = retrofit.create(ComunicacaoServidorMP.class);
        Call<JsonObject> request = conexao_pagamento.enviarPagamento(url,dados);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                String preferenceId = response.body() != null ? response.body().get("id").getAsString() : null;
                criarPagamento(preferenceId);
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

            }
        });
    }

    private void criarPagamento(String preferenceId){

        final AdvancedConfiguration advancedConfiguration =
                new AdvancedConfiguration.Builder().setBankDealsEnabled(false).build();
        new MercadoPagoCheckout
                .Builder(PUBLIC_KEY, preferenceId)
                .setAdvancedConfiguration(advancedConfiguration).build()
                .startPayment(this, 123);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123) {
            if (resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE) {

                final Payment pagamento = (Payment) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_PAYMENT_RESULT);
                assert pagamento != null;
                respostaMercadoPago(pagamento);

            }
        }
    }

    private void respostaMercadoPago(Payment pagamento){

        String status = pagamento.getPaymentStatus();

        String bairro = binding.editBairro.getText().toString();
        String rua_numero = binding.editRua.getText().toString();
        String cidade_estado = binding.editCidade.getText().toString();
        String celular = binding.editCelular.getText().toString();

        String endereco = "Bairro: " + bairro + " " + " Rua e Número: " + " " + rua_numero + " Cidade e Estado: " + " " + cidade_estado;
        String status_pagamento = "Status de Pagamento: " + " " + "Pagamento Aprovado";
        String status_entrega = "Status de Entrega: " + " " + "Em andamento";

        String nomeProduto = "Nome: " + " " + nome;
        String precoProduto = "Preço: " + " " + preco;
        String tamanho = "Tamanho do Calçado: " + " " + tamanhoCalcado;
        String celular_usuario = "Celular: " + " " + celular;

        if (status.equalsIgnoreCase("approved")){
            Snackbar snackbar = Snackbar.make(binding.container,"Sucesso ao fazer o pagamento",Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.BLUE);
            snackbar.setTextColor(Color.WHITE);
            snackbar.show();
            db.salvarDadosPedidoUsuario(endereco,celular_usuario,nomeProduto,precoProduto,tamanho,status_pagamento,status_entrega);
        }else if (status.equalsIgnoreCase("rejected")){
            Snackbar snackbar = Snackbar.make(binding.container,"Erro ao fazer o pagamento",Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.RED);
            snackbar.setTextColor(Color.WHITE);
            snackbar.show();
        }
    }
}