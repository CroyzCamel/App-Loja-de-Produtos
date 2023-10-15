package com.agenda.aplicativodelojavirturalcommercadopago.activities.activities.DetalhesProduto

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.agenda.aplicativodelojavirturalcommercadopago.R
import com.agenda.aplicativodelojavirturalcommercadopago.activities.activities.Pagamento.Pagamento
import com.agenda.aplicativodelojavirturalcommercadopago.databinding.ActivityDetalhesProdutoBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

class DetalhesProduto : AppCompatActivity() {

    private lateinit var binding: ActivityDetalhesProdutoBinding
    var tamanho_calcado = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalhesProdutoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this,R.color.dark_blue)

        val foto = intent.extras?.getString("foto")
        val nome = intent.extras?.getString("nome")
        val preco = intent.extras?.getString("preco")

        Glide.with(this@DetalhesProduto).load(foto).into(binding.dtFotoProduto)
        binding.dtNomeProduto.text = nome
        binding.precoProduto.text = "R$ ${preco}"

        binding.btFinalizarPedido.setOnClickListener {
            val tamanho_calcado_38 = binding.tamanho38
            val tamanho_calcado_39 = binding.tamanho39
            val tamanho_calcado_40 = binding.tamanho40
            val tamanho_calcado_41 = binding.tamanho41
            val tamanho_calcado_42 = binding.tamanho42

            when(true){
                tamanho_calcado_38.isChecked -> tamanho_calcado = "38"
                tamanho_calcado_39.isChecked -> tamanho_calcado = "38"
                tamanho_calcado_40.isChecked -> tamanho_calcado = "40"
                tamanho_calcado_41.isChecked -> tamanho_calcado = "41"
                tamanho_calcado_42.isChecked -> tamanho_calcado = "42"
                else -> {}
            }

            if (!tamanho_calcado_38.isChecked &&
                !tamanho_calcado_39.isChecked &&
                !tamanho_calcado_40.isChecked &&
                !tamanho_calcado_41.isChecked &&
                !tamanho_calcado_42.isChecked){

                val snackbar = Snackbar.make(it, "Escolha o tamanho do calçado", Snackbar.LENGTH_LONG)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.setTextColor(Color.WHITE)
                snackbar.show()
            } else {
                val intent = Intent(this@DetalhesProduto, Pagamento::class.java)
                intent.putExtra("tamanho_calcado",tamanho_calcado)
                intent.putExtra("nome",nome)
                intent.putExtra("preco",preco)
                startActivity(intent)
            }
        }
    }
}

