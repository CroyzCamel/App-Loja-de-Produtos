package com.agenda.aplicativodelojavirturalcommercadopago.activities.activities.TelaPrincipal

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.agenda.aplicativodelojavirturalcommercadopago.R
import com.agenda.aplicativodelojavirturalcommercadopago.activities.Adapter.AdapterProduto
import com.agenda.aplicativodelojavirturalcommercadopago.activities.activities.Formlogin.FormLogin
import com.agenda.aplicativodelojavirturalcommercadopago.activities.dialog.DialogPerfilUsuario
import com.agenda.aplicativodelojavirturalcommercadopago.activities.model.DB
import com.agenda.aplicativodelojavirturalcommercadopago.activities.model.Produto
import com.agenda.aplicativodelojavirturalcommercadopago.databinding.ActivityTelaPrincipalBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class TelaPrincipal : AppCompatActivity() {

    private lateinit var binding: ActivityTelaPrincipalBinding
    private lateinit var adapterProduto: AdapterProduto
    private lateinit var auth: FirebaseAuth
    var lista_produtos: MutableList<Produto> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelaPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recycler_produtos = binding.recyclerProdutos
        recycler_produtos.layoutManager = GridLayoutManager(this,2)
        recycler_produtos.setHasFixedSize(true)
        adapterProduto = AdapterProduto(this@TelaPrincipal,lista_produtos)
        recycler_produtos.adapter = adapterProduto


        auth = Firebase.auth
        val db = DB()
        db.obterListaDeProdutos(lista_produtos,adapterProduto)

        //Costumização statusbar e ActionBar
        window.statusBarColor = ContextCompat.getColor(this,R.color.dark_blue)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.dark_blue)))
        val corTitulo = resources.getColor(R.color.light_gray)
        supportActionBar?.setTitle(Html.fromHtml("<font color='$corTitulo'>Loja Virtual</font>"))

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.perfil -> iniciarDialagoPerfilUsuario()
            R.id.pedidos ->Log.d("p","Pedidos")
            R.id.deslogar -> deslogarUsuario()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun iniciarDialagoPerfilUsuario(){
        val dialogPerfilUsuario = DialogPerfilUsuario(this@TelaPrincipal)
        dialogPerfilUsuario.iniciarPerfilusuario()
        dialogPerfilUsuario.recuperarDadosUsuariosBanco()
    }

    private fun deslogarUsuario(){
        auth.signOut()
        startActivity(Intent(this@TelaPrincipal, FormLogin::class.java))
        finish()
    }


}