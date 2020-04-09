package com.example.systurnomobile.Herramientas

import android.content.Intent
import android.view.View
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.systurnomobile.IniciarSesion
import com.example.systurnomobile.MenuPrincipal
import com.example.systurnomobile.R
import kotlin.concurrent.thread

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
            IniciarSesion::class.java)
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
            IniciarSesion::class.java)
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
                    // Este dialogo solo muestra una advertencia, que se establece con el parámetro 'mensaje'
                })
        }

        constr.setMessage(mensaje)
            .setTitle(titulo)

        val dialogo: AlertDialog? = constr.create()
        return dialogo
    }

    /**
     * Permite generar un diálogo con advertencia en la pantalla
     * y acepta una acción que se realizará al presionar el botón 'Aceptar'
     */
    fun mostrarAdvertencia(titulo: String,
                           mensaje:String,
                           ctx:Context,
                           accion:()->Unit
    ): AlertDialog? {
        val constr: AlertDialog.Builder = ctx?.let{
            AlertDialog.Builder(it)
        }
        constr.apply {
            setPositiveButton("Aceptar",
                DialogInterface.OnClickListener { dialog, id ->
                    accion()
                })
        }

        constr.setMessage(mensaje)
            .setTitle(titulo)

        val dialogo: AlertDialog? = constr.create()
        return dialogo
    }

    /**
     * Permite generar un diálogo con pregunta en la pantalla
     * y acepta dos acciónes que se realizarán al presionar el botón 'Aceptar'
     * o el botón 'Cancelar'
     * @param titulo El título de la ventana de diálogo
     * @param mensaje El mensaje para el usuario
     * @param ctx El contexto de la actividad donde se mostrará el diálogo
     * @param accionSi Acción a tomar si el usuario presiona en 'Aceptar'
     * @param accionNo Acción a tomar si el usuario presiona en 'Cancelar'
     */
    fun mostrarDialogoPregunta(titulo: String,
                           mensaje:String,
                           ctx:Context,
                           accionSi:()->Unit,
                           accionNo:()->Unit
    ): AlertDialog? {
        val constr: AlertDialog.Builder = ctx?.let{
            AlertDialog.Builder(it)
        }
        constr.apply {
            setPositiveButton("Aceptar",
                DialogInterface.OnClickListener { dialog, id ->
                    accionSi()
                })
        }
        constr.apply {
            setNegativeButton("Cancelar",
                DialogInterface.OnClickListener {dialog, id->
                    accionNo()
                })
        }

        constr.setMessage(mensaje)
            .setTitle(titulo)

        val dialogo: AlertDialog? = constr.create()
        return dialogo
    }

    fun mostrarDialogoEspera(ctx: Context):AlertDialog?{
        val constr: AlertDialog.Builder = ctx?.let{
            AlertDialog.Builder(it)
        }
        constr.setMessage("Espere un momento, por favor...")
        constr.setView(R.layout.dialogo_espera)
        val dialogo = constr.create()
        return dialogo
    }

    fun ejecutarEsperando(accionQueEspera: () -> Unit,accionQueRetrasa: () -> Unit){
        val thread:Thread = thread (start = true){
            accionQueRetrasa()
        }
        thread.join()
        accionQueEspera()
    }


}