package com.example.systurnomobile.Fragmentos

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.systurnomobile.BDD.MedicamentoRecetado
import com.example.systurnomobile.R

class SolicitarTurnoRclrViewHolder (inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.solicitar_turno_item,parent,false))
{

    private var tv_med_nombre: TextView? = null
    private var tv_cant: TextView? = null
    private var tv_especialista: TextView? = null
    private var tv_fecha: TextView? = null
    private var tv_disponibilidad:TextView?=null

    init {
        tv_med_nombre = itemView.findViewById(R.id.tv_solicitarTurno_med_nombre)
        tv_cant = itemView.findViewById(R.id.tv_solicitarTurno_cant)
        tv_especialista = itemView.findViewById(R.id.tv_solicitarTurno_esp_nombre)
        tv_fecha = itemView.findViewById(R.id.tv_solicitarTurno_fecha)
        tv_disponibilidad = itemView.findViewById(R.id.tv_solicitarTurno_disponibilidad)
    }

    fun bind(dato: MedicamentoRecetado?){
        tv_med_nombre?.text = dato?.med_nombre
        tv_cant?.text = dato?.cantidad.toString()
        tv_especialista?.text = dato?.especialista
        tv_fecha?.text = dato?.fecha
        tv_disponibilidad?.text = dato?.disponibilidad
    }


}