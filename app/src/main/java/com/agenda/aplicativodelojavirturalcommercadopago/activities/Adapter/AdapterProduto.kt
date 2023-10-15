package com.agenda.aplicativodelojavirturalcommercadopago.activities.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agenda.aplicativodelojavirturalcommercadopago.activities.activities.DetalhesProduto.DetalhesProduto
import com.agenda.aplicativodelojavirturalcommercadopago.activities.model.Produto
import com.agenda.aplicativodelojavirturalcommercadopago.databinding.ProdutoItemBinding
import com.bumptech.glide.Glide

class AdapterProduto(val context: Context, val lista_produtos: MutableList<Produto>):
    RecyclerView.Adapter<AdapterProduto.ProdutoViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
       val item_lista = ProdutoItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ProdutoViewHolder(item_lista)
    }

    override fun getItemCount(): Int = lista_produtos.size

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        Glide.with(context).load(lista_produtos[position].foto).into(holder.fotoProduto)
        holder.nomeProduto.text = lista_produtos[position].nome
        holder.precoProduto.text = "R$ ${lista_produtos[position].preco}"

        holder.itemView.setOnClickListener {
            val intent = Intent(context,DetalhesProduto::class.java)
            intent.putExtra("foto",lista_produtos[position].foto)
            intent.putExtra("nome",lista_produtos[position].nome)
            intent.putExtra("preco",lista_produtos[position].preco)
            context.startActivity(intent)
        }
    }

    inner class ProdutoViewHolder(binding: ProdutoItemBinding) : RecyclerView.ViewHolder(binding.root){
        val fotoProduto = binding.fotoProduto
        val nomeProduto = binding.nomeProduto
        val precoProduto = binding.precoProduto
    }
}