package com.agenda.aplicativodelojavirturalcommercadopago.activities.dialog

import android.app.Activity
import android.app.AlertDialog
import com.agenda.aplicativodelojavirturalcommercadopago.R
                        //Instanciamento de uma activity
class DialogCarregando(private val activity: Activity) {

    lateinit var dialog: AlertDialog

    fun iniciarCarregamentoAlertDialog(){
        //Passando a activity
        val builder = AlertDialog.Builder(activity)
        //Inflando o layout
        val layoutInflater = activity.layoutInflater
        //Escolhando o XMl usado
        builder.setView(layoutInflater.inflate(R.layout.dialog_carregando,null))
        builder.setCancelable(true)
        //Criando o builder
        dialog = builder.create()
        //Execucação da dialog
        dialog.show()
    }


    fun liberarAlertDialog(){
        dialog.dismiss()
    }
}