package com.example.login

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.systurnomobile.BDD.MisTurnos
import com.example.systurnomobile.BDD.Turno
import com.example.systurnomobile.R

/**
 * Esta clase manipula las vistas repetitivas que se usarán en el ReciclerView
 */
class HistorialRclrViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.historial_item, parent, false))
{
    private var tv_fecha: TextView? = null
    private var tv_hora: TextView? = null
    private var tv_estado: TextView? = null
    private var tv_id: TextView?=null

    //Inicializamos los elementos que recibirán los datos en el ReciclerView
    init {
        tv_fecha = itemView.findViewById(R.id.tv_historialItem_fecha)
        tv_hora = itemView.findViewById(R.id.tv_historialItem_hora)
        tv_estado = itemView.findViewById(R.id.tv_historialItem_estado)
        tv_id = itemView.findViewById(R.id.tv_historialItem_ID)
    }

    //Añadimos los datos que mostraremos desde el parámetro
    fun bind(dato: MisTurnos){
        var fechahora = dato.fechahora.split(" ")
        tv_fecha?.text = fechahora[0]
        tv_hora?.text = fechahora[1]
        tv_estado?.text = dato.estado.toUpperCase()
        tv_id?.text=dato.id.toString()
    }
}