package com.example.login

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistorialRclrViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.historial_item, parent, false))
{
    private var tv_fecha: TextView? = null
    private var tv_hora: TextView? = null
    private var tv_estado: TextView? = null

    init {
        tv_fecha = itemView.findViewById(R.id.tv_fecha)
        tv_hora = itemView.findViewById(R.id.tv_hora)
        tv_estado = itemView.findViewById(R.id.tv_estado)
    }

    fun bind(dato: Dato){
        tv_fecha?.text = dato.fecha
        tv_hora?.text = dato.hora
        tv_estado?.text = dato.estado
    }
}