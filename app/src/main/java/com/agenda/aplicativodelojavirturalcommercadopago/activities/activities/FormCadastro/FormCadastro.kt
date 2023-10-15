package com.agenda.aplicativodelojavirturalcommercadopago.activities.activities.FormCadastro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.agenda.aplicativodelojavirturalcommercadopago.activities.model.DB
import com.agenda.aplicativodelojavirturalcommercadopago.databinding.ActivityFormCadastroBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FormCadastro : AppCompatActivity() {

    private lateinit var binding: ActivityFormCadastroBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val db = DB()


    binding.btCadastrar.setOnClickListener {it ->

        val nome = binding.editNome.text.toString()
        val email = binding.editEmail.text.toString()
        val senha = binding.editSenha.text.toString()

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            val snackbar = Snackbar.make(it,"Preencha todos os campos!", Snackbar.LENGTH_SHORT).show()
        } else {
            Firebase.auth.createUserWithEmailAndPassword(email,senha).addOnCompleteListener{ tarefa ->
                    if (tarefa.isSuccessful){
                    db.salvarDadosUsuario(nome)

                    }
                }
            }

        }
    }
    private fun criarConta(email: String, senha: String){
        val db =DB()
        val nome = binding.editNome.text.toString()


        db.salvarDadosUsuario(nome)
        auth.createUserWithEmailAndPassword(email,senha)
            .addOnCompleteListener { autenticacao ->
                if (autenticacao.isSuccessful){
                    Toast.makeText(
                        baseContext,
                        "Cadastro realizado com sucesso!",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            } .addOnFailureListener { erro ->
                val mensagemErro = when(erro) {
                    is FirebaseAuthWeakPasswordException -> "Digite uma senha boa"
                    is FirebaseAuthUserCollisionException -> "Conta já criada"
                    is FirebaseNetworkException -> "Sem conexão com a internet"
                    else -> "Erro ao cadastra usuario"
                }
                val toast = Toast.makeText(
                    baseContext,
                    mensagemErro,
                    Toast.LENGTH_SHORT,
                ).show()
            }
    }

}

