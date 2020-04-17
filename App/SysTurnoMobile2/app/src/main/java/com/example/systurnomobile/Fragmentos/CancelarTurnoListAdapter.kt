package com.example.systurnomobile.Fragmentos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.systurnomobile.BDD.MedicamentoRecetado
import java.text.FieldPosition

class CancelarTurnoListAdapter(private val lista:List<MedicamentoRecetado>): RecyclerView.Adapter<CancelarTurnoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):CancelarTurnoViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        return CancelarTurnoViewHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder:CancelarTurnoViewHolder, position: Int){
        val dato: MedicamentoRecetado? = if(lista==null){
            MedicamentoRecetado(0,0,"jaja","",0,"",0,"","")
        }else{
            lista?.get(position)
        }
        holder.bind(dato)
    }

    override fun getItemCount(): Int = lista?.size ?: 0

}