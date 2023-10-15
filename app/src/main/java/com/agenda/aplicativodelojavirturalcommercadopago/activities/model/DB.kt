package com.agenda.aplicativodelojavirturalcommercadopago.activities.model

import android.annotation.SuppressLint
import android.util.Log
import android.widget.TextView
import com.agenda.aplicativodelojavirturalcommercadopago.activities.Adapter.AdapterProduto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.UUID

class DB {

    @SuppressLint("SuspiciousIndentation")
    fun salvarDadosUsuario(nome: String){
        val usuarioID = Firebase.auth.currentUser!!.uid
        val db = Firebase.firestore

        val usuario = hashMapOf(
            "nome" to nome,
        )

    val documentReferece: DocumentReference = db.collection("Usuarios").document(usuarioID)
        documentReferece.set(usuario).addOnSuccessListener {
            Log.d("DB", "Dados salvos com sucesso")
        }.addOnFailureListener { e ->
            Log.w("DB", "Erro ao salvar os dados", e)
        }
    }
    fun recuperarDadosUsuarioPerfil(nomeUsuario: TextView, emailUsuario: TextView){
        val usuarioID = Firebase.auth.currentUser!!.uid
        val email = Firebase.auth.currentUser!!.email
        val db  = Firebase.firestore

        val documentReference: DocumentReference = db.collection("Usuarios").document(usuarioID)
        documentReference.addSnapshotListener { documento, error ->
            if (documento != null){
                nomeUsuario.text = documento.getString("nome")
                emailUsuario.text = email
            }
        }
    }

    fun obterListaDeProdutos(lista_produtos: MutableList<Produto>, adapter_Produto: AdapterProduto){
        val db = Firebase.firestore
        db.collection("Produtos").get()
            .addOnCompleteListener { tarefa ->
                if (tarefa.isSuccessful)
                    for (documento in tarefa.result!!){
                        val produtos = documento.toObject(Produto::class.java)
                        lista_produtos.add(produtos)
                        adapter_Produto.notifyDataSetChanged()
                    }
            }
    }
    fun salvarDadosPedidoUsuario(
        endereco: String,
        celular: String,
        produto: String,
        preco: String,
        tamanho_calcado: String,
        status_pagamento: String,
        status_entrega: String,
    ){
        var db = Firebase.firestore
        var usuarioID = Firebase.auth.currentUser!!.uid
        var pedidoID = UUID.randomUUID().toString()

        val pedidos = hashMapOf(
            "endereco" to endereco,
            "celular" to celular,
            "produto" to produto,
            "preco" to preco,
            "tamanho_calcado" to tamanho_calcado,
            "status_pagamento" to status_pagamento,
            "status_entrega" to status_entrega
        )

        val documentReference = db.collection("Usuario_Pedidos").document(usuarioID)
            .collection("Pedidos").document(pedidoID)
        documentReference.set(pedidos).addOnSuccessListener {
            Log.d("db_pedido", "Sucesso ao salvar os pedidos!")
        }
    }
}