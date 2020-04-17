package com.example.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.systurnomobile.BDD.MisTurnos
import com.example.systurnomobile.BDD.Turno


/**
 * Clase necesaria para el manejo de ReciclerView Esta clase implementa la visualización
 */
class HistorialListAdapter (private val lista: MutableList<MisTurnos>):RecyclerView.Adapter<HistorialRclrViewHolder>()   {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorialRclrViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HistorialRclrViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: HistorialRclrViewHolder, position: Int) {
        val dato: MisTurnos = lista[position]
        holder.bind(dato)
    }

    override fun getItemCount(): Int = lista.size

    fun actualizarDatos(turno: MisTurnos){
        lista.add(turno)
        notifyDataSetChanged()
    }
}