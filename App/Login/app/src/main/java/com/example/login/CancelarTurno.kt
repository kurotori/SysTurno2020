package com.example.login

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.cancelar_turno_2.*
import kotlinx.android.synthetic.main.reserva_turno_2.*
import kotlinx.android.synthetic.main.reserva_turno_2.tv_reservaHora
import kotlinx.android.synthetic.main.reserva_turno_2.tv_reservaNum

class CancelarTurno : AppCompatActivity() {

    var estado:Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cancelar_turno_2)
    }

    fun cancelarTurno(v:View){
        if(estado){
            var titulo:String = "Cancelar Reserva"
            var mensaje:String = "¿Confirma que desea cancelar este turno?"
            //Creo el dialogo
            val dialogo = mostrarDialogo(titulo,mensaje, v.context)
            //lo muestro
            dialogo?.show()
        }
        else{
            var titulo:String = "No Hay Turnos Reservados"
            var mensaje:String = "No hay ningún turno para cancelar en el sistema."
            //Creo el dialogo
            val dialogo = mostrarAdvertencia(titulo,mensaje, v.context)
            //lo muestro
            dialogo?.show()
        }
    }

    fun volverAMenu(v:View){
        this.finish()
    }

    //Métodos para generar diálogos
    private fun mostrarDialogo(titulo: String, mensaje:String, ctx: Context): AlertDialog? {
        val constr: AlertDialog.Builder = ctx?.let{
            AlertDialog.Builder(it)
        }
        constr.apply {
            setPositiveButton("Aceptar",
                DialogInterface.OnClickListener { dialog, id ->
                    // Si se hizo click en Aceptar
                    //1 - Se debe retirar de la vista los medicamentos DISPONIBLES
                    tblrw_medRes_01.visibility= View.GONE
                    tblrw_medRes_02.visibility= View.GONE

                    //2 - Se debe adelantar el número y la hora al siguiente turno disponible
                    tv_CancReservaHora.text = ""
                    tv_CancReservaNum.text = ""
                    tv_CancReservaFecha.text=""

                    //3 - Se actualiza el estado
                    estado = false
                })
            setNegativeButton("Cancelar",
                DialogInterface.OnClickListener { dialog, id ->
                    // User cancelled the dialog
                })
        }

        constr.setMessage(mensaje)
            .setTitle(titulo)

        val dialogo: AlertDialog? = constr.create()
        return dialogo
    }

    private fun mostrarAdvertencia(titulo: String, mensaje:String, ctx: Context): AlertDialog? {
        val constr: AlertDialog.Builder = ctx?.let{
            AlertDialog.Builder(it)
        }
        constr.apply {
            setPositiveButton("Aceptar",
                DialogInterface.OnClickListener { dialog, id ->
                    // Este dialogo solo muestra una advertencia, que se establece con el mensaje
                })
        }

        constr.setMessage(mensaje)
            .setTitle(titulo)

        val dialogo: AlertDialog? = constr.create()
        return dialogo
    }
}
