package com.example.systurnomobile.Fragmentos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.login.HistorialRclrFragment
import com.example.login.HistorialRclrViewHolder
import com.example.systurnomobile.BDD.MedicamentoRecetado

class SolicitarTurnoListAdapter(private val lista:List<MedicamentoRecetado>?):RecyclerView.Adapter<SolicitarTurnoRclrViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SolicitarTurnoRclrViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SolicitarTurnoRclrViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: SolicitarTurnoRclrViewHolder, position: Int) {
        val dato: MedicamentoRecetado? = if(lista==null){
            MedicamentoRecetado(0,0,"jaja","",0,"",0,"","")
        }else{
            lista?.get(position)
        }
        holder.bind(dato)
    }

    override fun getItemCount(): Int = lista?.size ?: 0

}