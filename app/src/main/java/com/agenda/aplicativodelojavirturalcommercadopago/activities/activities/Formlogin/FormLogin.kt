package com.agenda.aplicativodelojavirturalcommercadopago.activities.activities.Formlogin

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.agenda.aplicativodelojavirturalcommercadopago.activities.activities.FormCadastro.FormCadastro
import com.agenda.aplicativodelojavirturalcommercadopago.activities.activities.TelaPrincipal.TelaPrincipal
import com.agenda.aplicativodelojavirturalcommercadopago.activities.dialog.DialogCarregando
import com.agenda.aplicativodelojavirturalcommercadopago.databinding.ActivityFormLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FormLogin : AppCompatActivity() {

    private lateinit var binding: ActivityFormLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth =Firebase.auth

        val dialogCarregando = DialogCarregando(this@FormLogin)

        binding.btEntrar.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()

            if (email.isEmpty() || senha.isEmpty()){
                val snackbar = Snackbar.make(it,"Preencha todos os campos",Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.setTextColor(Color.WHITE )
                snackbar.show()
            } else {
                dialogCarregando.iniciarCarregamentoAlertDialog()
                Handler(Looper.getMainLooper()).postDelayed({
                    logarConta(email,senha)
                },30000)
                dialogCarregando.liberarAlertDialog()
              }

        }

        binding.txtTelaCadastro.setOnClickListener {
            startActivity(Intent(this@FormLogin, FormCadastro::class.java))
        }

    }

    private fun logarConta(email:String,senha:String,){
        auth.signInWithEmailAndPassword(email,senha)
            .addOnCompleteListener { login ->

                if(login.isSuccessful){
                    dialogCarregando()
                    startActivity(Intent(this@FormLogin, TelaPrincipal::class.java))
                    finish()
                }
            }
    }
     private fun dialogCarregando(){
         dialogCarregando()
     }

    override fun onStart() {
        super.onStart()

        val usuarioatual = auth.currentUser
        if (usuarioatual != null){
            reload()
        }

    }

    private fun reload() {
        startActivity(Intent(this@FormLogin, TelaPrincipal::class.java))
    }
}