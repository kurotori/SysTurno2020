package com.example.systurnomobile.Herramientas

import android.content.Intent
import android.view.View
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.systurnomobile.MainActivity
import com.example.systurnomobile.MenuPrincipal

class ManejoDeGUI {

    /**
     * Permite cambiar de Layout
     */
    fun cambiarLayout(){

    }

    /**
     * Abre la actividad Menú Principal
     */
    fun irAMenuPrincipal(v: View){
        val ctx = v.context
        val intent: Intent = Intent(ctx,
            MenuPrincipal::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("EXIT", true)
        ContextCompat.startActivity(ctx,intent,null)
    }

    fun irAMenuPrincipal(ctx: Context){
        val intent: Intent = Intent(ctx,
            MenuPrincipal::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("EXIT", true)
        ContextCompat.startActivity(ctx,intent,null)
    }

    /**
     * Abre la actividad que permite Iniciar Sesión
     */
    fun irAIniciarSesion(v:View){
        val ctx = v.context
        val intent: Intent = Intent(ctx,
            MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("EXIT", true)
        ContextCompat.startActivity(ctx,intent,null)
    }

    /**
     * Abre la actividad que permite Iniciar Sesión
     */
    fun irAIniciarSesion(ctx: Context){

        val intent: Intent = Intent(ctx,
            MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("EXIT", true)
        ContextCompat.startActivity(ctx,intent,null)
    }

    /**
     * Permite generar un diálogo con advertencia en la pantalla
     */
    fun mostrarAdvertencia(titulo: String, mensaje:String, ctx:Context): AlertDialog? {
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