package com.example.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.systurnomobile.R
import kotlinx.android.synthetic.main.historial_recicler.*

data class Dato(val fecha:String, val hora:String, val estado:String)

class HistorialRclrFragment : Fragment(){

    //Estos son los datos que mostrar en el ReciclerView. Acá se deberían solicitar los datos del
    //servidor
    val datos = listOf<Dato>(
        Dato("01/03/2020","9:30","usado"),
        Dato("02/03/2020","9:45","cancelado"),
        Dato("01/03/2020","9:30","usado"),
        Dato("01/03/2020","9:30","usado"),
        Dato("01/03/2020","9:30","confirmado")
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    // inflate your view here
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.historial_recicler, container, false)


    // Wait until your View is guaranteed not null to grab View elements
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listado_recicler.apply {
            // set a LinearLayoutManager to handle Android
                // RecyclerView behavior
                layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
                adapter = HistorialListAdapter(datos) }
        // find your view elements and do stuff here
    }

    companion object {
        fun newInstance() = HistorialRclrFragment()
    }
}