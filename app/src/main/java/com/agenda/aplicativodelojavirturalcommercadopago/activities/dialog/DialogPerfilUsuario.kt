package com.agenda.aplicativodelojavirturalcommercadopago.activities.dialog

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import com.agenda.aplicativodelojavirturalcommercadopago.activities.activities.Formlogin.FormLogin
import com.agenda.aplicativodelojavirturalcommercadopago.activities.model.DB
import com.agenda.aplicativodelojavirturalcommercadopago.databinding.DialogPerfilUsuarioBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class DialogPerfilUsuario(private val activity: Activity) {

        lateinit var dialog: AlertDialog
        lateinit var binding: DialogPerfilUsuarioBinding

        fun iniciarPerfilusuario(){
            binding = DialogPerfilUsuarioBinding.inflate(activity.layoutInflater)

            val builder = AlertDialog.Builder(activity)
            builder.setView(binding.root)
            builder.setCancelable(true)

            dialog = builder.create()
            dialog.show()

        }

        fun recuperarDadosUsuariosBanco(){
            val nomeUsuario = binding.txtNomeUsuario
            val emailUsuario = binding.txtEmailUsuario
            val db = DB()
            db.recuperarDadosUsuarioPerfil(nomeUsuario, emailUsuario)

            binding.btDeslogar.setOnClickListener {
                Firebase.auth.signOut()
                activity.startActivity(Intent(activity, FormLogin::class.java))
                activity.finish()
            }
        }
    }