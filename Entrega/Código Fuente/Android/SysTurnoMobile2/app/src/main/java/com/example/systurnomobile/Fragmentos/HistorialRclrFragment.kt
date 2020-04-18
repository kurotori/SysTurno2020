package com.example.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.systurnomobile.BDD.ManejoBDD
import com.example.systurnomobile.BDD.MisTurnos
import com.example.systurnomobile.BDD.Turno
import com.example.systurnomobile.Herramientas.ManejoURL
import com.example.systurnomobile.R
import kotlinx.android.synthetic.main.historial_recicler.*
import kotlin.concurrent.thread

//data class Dato(val fecha:String, val hora:String, val estado:String)

class HistorialRclrFragment() : Fragment(){

    private val manejoURL = ManejoURL()
    private val manejoBDD = ManejoBDD()
    private var historialListAdapter: HistorialListAdapter? = null
    //Estos son datos de ejemplo que mostrar en el ReciclerView.
    val datos = listOf<Turno>(
        Turno(0,"01/03/2020 09:30","usado"),
        Turno(1,"02/03/2020 09:45","cancelado"),
        Turno(2,"01/03/2020 09:30","usado"),
        Turno(3,"01/03/2020 09:30","usado"),
        Turno(4,"01/03/2020 09:30","confirmado")
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    // inflate your view here
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        //historial_recicler es el layout donde establecemos la aparience de la actividad
        // que contiene el ReciclerView
        inflater.inflate(R.layout.historial_recicler, container, false)


    // Wait until your View is guaranteed not null to grab View elements
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var datos2:MutableList<MisTurnos>? = null

        val ctx = this.context
        val usuario = manejoBDD.leerDatosUsuario(ctx!!)
        val sesion = manejoBDD.leerSesion(ctx!!)

        if(ctx!=null){
            datos2 = manejoBDD.leerMisTurnos(ctx!!)
        }

        historialListAdapter = HistorialListAdapter(datos2!!)

        with(listado_recicler){
            layoutManager = LinearLayoutManager(activity)
            adapter = historialListAdapter
        }
        historialListAdapter?.notifyDataSetChanged()

        //listado_recicler se encuentra en historial_recicler.xml en los layouts
       // listado_recicler.apply {
            // set a LinearLayoutManager to handle Android
                // RecyclerView behavior

            // set the custom adapter to the RecyclerView

           // }



        // find your view elements and do stuff here
    }

    companion object {
        fun newInstance() = HistorialRclrFragment()
    }
}