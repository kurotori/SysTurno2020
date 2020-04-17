package com.example.systurnomobile.Fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.systurnomobile.BDD.ManejoBDD
import com.example.systurnomobile.BDD.MedicamentoRecetado
import com.example.systurnomobile.BDD.MisTurnos
import com.example.systurnomobile.Herramientas.ManejoDeGUI
import com.example.systurnomobile.Herramientas.ManejoURL
import com.example.systurnomobile.R
import kotlinx.android.synthetic.main.cancelar_turno_recicler.*

class CancelarTurnoRclrFragment(): Fragment() {
    private val manejoURL = ManejoURL()
    private val manejoBDD = ManejoBDD()
    val manejoDeGUI = ManejoDeGUI()
    var datos:List<MedicamentoRecetado>?=ArrayList<MedicamentoRecetado>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    // inflate your view here
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.cancelar_turno_recicler,container,false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var datosMeds :List<MedicamentoRecetado> = ArrayList()
        var datosTurno: MisTurnos? = null

        val ctx = this.context

        if(ctx!=null){
            datosMeds = manejoBDD.leerMedicamentosRecetados(ctx)!!
            datosTurno = manejoBDD.leerMisTurnos(ctx)[0]

            var fechahora = datosTurno.fechahora.split(" ")
            tv_CancReserva_Fecha.text = fechahora[0]
            tv_CancReserva_Hora.text = fechahora[1]
            tv_CancReserva_Num.text = datosTurno.id.toString()
        }

        cancelar_turno_recicler.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = CancelarTurnoListAdapter(datosMeds!!)
        }

        //Al presionar el botón Cancelar Turno
        btn_CancTurno_Cancelar.setOnClickListener {
            if(ctx!=null){
                manejoDeGUI.mostrarDialogoPregunta(
                    "Cancelar Turno",
                    "Va a cancelar este turno.\n¿Desea Continuar?",
                    ctx,
                    {
                        var usuario = manejoBDD.leerDatosUsuario(ctx)
                        var sesion = manejoBDD.leerSesion(ctx)
                        if (usuario != null && sesion != null){
                            manejoURL.cancelarTurnoSolicitado(ctx, usuario, sesion)
                        }
                    },
                    {
                    }
                )?.show()
            }
        }

    }

    companion object{
        fun newInstance() = CancelarTurnoRclrFragment()
    }


}