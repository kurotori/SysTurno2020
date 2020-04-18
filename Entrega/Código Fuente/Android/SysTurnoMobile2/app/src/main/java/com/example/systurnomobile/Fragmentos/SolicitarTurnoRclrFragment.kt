package com.example.systurnomobile.Fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.systurnomobile.BDD.*
import com.example.systurnomobile.Herramientas.ManejoDeGUI
import com.example.systurnomobile.Herramientas.ManejoURL
import com.example.systurnomobile.R
import kotlinx.android.synthetic.main.solicitar_turno_recicler.*
import kotlin.concurrent.thread


class SolicitarTurnoRclrFragment():Fragment(){

    val manejoURL = ManejoURL()
    val manejoBDD = ManejoBDD()
    val manejoDeGUI = ManejoDeGUI()

    var usuario:Usuario?=null
    var sesion:Sesion?=null
    var datos:List<MedicamentoRecetado>?=ArrayList<MedicamentoRecetado>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.solicitar_turno_recicler,container,false)

    // Wait until your View is guaranteed not null to grab View elements
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // find your view elements and do stuff here

        //Búsqueda de datos
        var usuario:Usuario?
        var sesion:Sesion?
        var turno: Turno?=null
        var ctx = this.context

        if(ctx!=null){
            usuario = manejoBDD.leerDatosUsuario(ctx)
            sesion = manejoBDD.leerSesion(ctx)
            if (usuario != null && sesion != null){
                val thread = thread(start = true){
                    manejoURL.buscarTurnosAbiertos(ctx,usuario!!,sesion!!)
                }
                thread.join()
                turno = manejoBDD.leerPrimerTurno(ctx)
                if(turno!=null){
                    mostrarDatosTurno(turno.id,turno.fechahora)
                }

            }
        }

        //rclrvw_solicitarTurno_listado se encuentra en solicitar_turno_recicler.xml en los layouts
        rclrvw_solicitarTurno_listado.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            datos = manejoBDD.leerMedicamentosRecetados(this.context)

            if(datos?.size==0){
                manejoDeGUI.mostrarAdvertencia("ATENCIÓN",
                    "No se encontraron medicamentos para retirar",
                    this.context
                )?.show()
                btn_solicitarTurno_Confirmar.isEnabled = false
            }
            adapter = SolicitarTurnoListAdapter(datos) }

        //Al presionar el boton de actualizar turnos
        btn_solicitarTurno_actTurno.setOnClickListener{
            if(ctx!=null){
                usuario = manejoBDD.leerDatosUsuario(ctx)
                sesion = manejoBDD.leerSesion(ctx)
                if (usuario != null && sesion != null){
                    var dialogo=manejoDeGUI.mostrarDialogoEspera(ctx)
                    dialogo?.show()
                    val thread = thread(start = true){
                        manejoURL.buscarTurnosAbiertos(ctx,usuario!!,sesion!!)
                    }
                    thread.join()
                    turno = manejoBDD.leerPrimerTurno(ctx)
                    if(turno!=null){
                        mostrarDatosTurno(turno!!.id,turno!!.fechahora)
                        dialogo?.dismiss()
                    }
                }
            }
        }

        //Al presionar el botón siguiente turno
        btn_solicitarTurno_turnoSig.setOnClickListener{
            if(ctx!=null){
                usuario = manejoBDD.leerDatosUsuario(ctx)
                sesion = manejoBDD.leerSesion(ctx)
                if (usuario != null && sesion != null){
                    turno = manejoBDD.leerTurnoSig(ctx,turno!!)
                    if(turno!=null){
                        mostrarDatosTurno(turno!!.id,turno!!.fechahora)
                    }
                }
            }
        }

        //Al presionar el botón turno previo
        btn_solicitarTurno_turnoPrev.setOnClickListener{
            if(ctx!=null){
                usuario = manejoBDD.leerDatosUsuario(ctx)
                sesion = manejoBDD.leerSesion(ctx)
                if (usuario != null && sesion != null){
                    turno = manejoBDD.leerTurnoAnt(ctx,turno!!)
                    if(turno!=null){
                        mostrarDatosTurno(turno!!.id,turno!!.fechahora)
                    }
                }
            }
        }

        //Al presionar el botón Confirmar
        btn_solicitarTurno_Confirmar.setOnClickListener{
            if(ctx!=null){
                usuario = manejoBDD.leerDatosUsuario(ctx)
                sesion = manejoBDD.leerSesion(ctx)
                if (usuario != null && sesion != null){
                   var turno_id = tv_solicitarturno_proxTurno_num.text.toString()
                   manejoURL.solicitarTurno(ctx,usuario!!,sesion!!,turno_id)
                }
            }
        }

    }

    companion object {
        fun newInstance() = SolicitarTurnoRclrFragment()
    }

    fun mostrarDatosTurno(id:Int, fechahora:String){
        var fecha:String=fechahora.split(" ")[0]
        var hora:String=fechahora.split(" ")[1]

        tv_solicitarTurno_proxTurno_hora.text = hora
        tv_solicitarturno_proxturno_fecha.text = fecha
        tv_solicitarturno_proxTurno_num.text = id.toString()
    }

}