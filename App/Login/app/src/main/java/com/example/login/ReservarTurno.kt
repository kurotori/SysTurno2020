package com.example.login

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.reserva_turno_2.*

class ReservarTurno : AppCompatActivity() {
    var estado: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reserva_turno_2)
    }

    public fun reservar(v: View){
        if(estado){
            var titulo:String = "Confirmar Reserva"
            var mensaje:String = "¿Desea solicitar este turno?"
            //Creo el dialogo
            val dialogo = mostrarDialogo(titulo,mensaje, v.context)
            //lo muestro
            dialogo?.show()
        }
        else{
            var titulo:String = "Ya Existe Una Reserva"
            var mensaje:String = "Usted ya tiene una reserva realizada."
            //Creo el dialogo
            val dialogo = mostrarAdvertencia(titulo,mensaje, v.context)
            //lo muestro
            dialogo?.show()
        }
    }

    public fun volverAMenu(v: View){
        this.finish()
    }

    //Métodos para generar diálogos
    private fun mostrarDialogo(titulo: String, mensaje:String, ctx:Context):AlertDialog? {
        val constr: AlertDialog.Builder = ctx?.let{
            AlertDialog.Builder(it)
        }
        constr.apply {
            setPositiveButton("Aceptar",
                DialogInterface.OnClickListener { dialog, id ->
                    // Si se hizo click en Aceptar
                    //1 - Se debe retirar de la vista los medicamentos DISPONIBLES
                    tblrw_med_01.visibility=View.GONE
                    tblrw_med_02.visibility=View.GONE

                    //2 - Se debe adelantar el número y la hora al siguiente turno disponible
                    tv_reservaNum.text = "11"
                    tv_reservaHora.text = "8:40"

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

        val dialogo:AlertDialog? = constr.create()
        return dialogo
    }

    private fun mostrarAdvertencia(titulo: String, mensaje:String, ctx:Context):AlertDialog? {
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

        val dialogo:AlertDialog? = constr.create()
        return dialogo
    }


}
