package com.example.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


/**
 * Clase necesaria para el manejo de ReciclerViews
 */
class HistorialListAdapter (private val lista: List<Dato>):RecyclerView.Adapter<HistorialRclrViewHolder>()   {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorialRclrViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HistorialRclrViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: HistorialRclrViewHolder, position: Int) {
        val dato: Dato = lista[position]
        holder.bind(dato)
    }

    override fun getItemCount(): Int = lista.size
}