package com.example.systurnomobile.Herramientas

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.systurnomobile.BDD.BaseDeDatos
import com.example.systurnomobile.BDD.ManejoBDD
import com.example.systurnomobile.BDD.Sesion
import com.example.systurnomobile.BDD.SesionDAO
import com.example.systurnomobile.ManejoDeGUI
import com.example.systurnomobile.MenuPrincipal
import io.reactivex.Completable.fromCallable
import io.reactivex.Flowable.fromCallable
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.newCoroutineContext
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.*
import kotlin.concurrent.thread
import kotlin.coroutines.*

class ManejoURL(ipServidor: String) {

    //public var ipServidor=""
    private val manejoBDD:ManejoBDD = ManejoBDD()
    private val manejoDeGUI:ManejoDeGUI = ManejoDeGUI()

    //private val servidor:String = "http://"+ipServidor+"/SysTurno2020/"
    private val servidor:String = "http://"+ipServidor+"/"

    public val urlPruebas: URL = URL(servidor+"pruebajson/")
    public val urlLogin:URL = URL(servidor+"login/")
    val urlValidarSesion:URL = URL(servidor+"validarSesion/")

    private val manejoJSON:ManejoJSON = ManejoJSON()


    /**
     * Función de prueba para comprobar acceso a los datos del servidor
     */
    public fun leerPrueba(ctx:Context, tv_destino: TextView){
        val queue = Volley.newRequestQueue(ctx)
        val url = urlPruebas.toString()

        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            Response.Listener<String> { response ->
                tv_destino.text = response
            },
            Response.ErrorListener { println("That didn't work!") })

        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    /**
     * Solicita un token al servidor e intenta iniciar sesión con el mismo
     */
    public fun obtenerToken(v:View,//ctx:Context,
                            ciUsuario: String,
                           contrasenia:String,
                            respuesta: Respuesta,
                            panelEspera: RelativeLayout
    ){
        val solicitud:Solicitud = Solicitud(urlLogin.toString(),
            {
                respuesta.respuesta = it.toString()

                iniciarSesion(
                    v,
                    ciUsuario,
                    contrasenia,
                    respuesta.tokenVal(),
                    respuesta.tokenId(),
                    respuesta
                )

                panelEspera.visibility = View.GONE
            },{
                println(it.toString())//tv_destino.text = it.toString()
            })
        solicitud.POST("usuario_ci" to ciUsuario)
    }


    /**
     * Genera el proceso de iniciar sesión en el servidor
     */
    public fun iniciarSesion(v:View,//ctx:Context,
                            ciUsuario: String,
                            contrasenia: String,
                             token_val:String,
                             token_id:String,
                            respuesta: Respuesta
    ){

        val solicitud:Solicitud = Solicitud(urlLogin.toString(),
            {
                val ctx = v.context
                respuesta.respuesta = it.toString()

                println("tipo: "+respuesta.tipo())
                println("mensaje: "+respuesta.mensaje())
                println("token ID: "+respuesta.tokenId())
                println("token Val: "+respuesta.tokenVal())
                println("sesion ID: "+respuesta.sesionId())
                println("sesion Val: "+respuesta.sesionVal())

                if(analizarRespuesta(respuesta,ctx)){
                    //Guarda los datos de sesión en la base de datos local
                    manejoBDD.guardarSesion(ctx, respuesta)
                    //Guarda los datos del Usuario en la base de datos local
                    // por ahora solo la CI
                    var ciUsr: Int = ciUsuario.toInt()
                    manejoBDD.guardarCiUsuario(ctx,ciUsr)
                    //Pasaje al menú principal
                    manejoDeGUI.irAMenuPrincipal(v)
                    /*val intent: Intent = Intent(ctx,MenuPrincipal::class.java)

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.putExtra("EXIT", true)
                    ContextCompat.startActivity(ctx,intent,null)*/
                }


            },{
                println(it.toString())//tv_destino.text = it.toString()
            })
        solicitud.POST(
            "usuario_ci" to ciUsuario,
            "contrasenia" to contrasenia,
            "token_val" to token_val,
            "token_id" to token_id
            )
    }

    /**
     * Permite analizar la respuesta recibida del servidor.
     * Devuelve 'false' si la respuesta tiene tipo 'ERROR'
     * de lo contrario devuelve 'true'
     */
    fun analizarRespuesta(respuesta:Respuesta, ctx: Context):Boolean{
        var resultado:Boolean = false

        if (respuesta.tipo().equals("ERROR")){
            resultado = false
            val adv = mostrarAdvertencia(respuesta.tipo(),respuesta.mensaje(),ctx)
            adv?.show()
        }
        else{
            resultado = true
        }
        return resultado
    }

    /**
     * Solicita una vlaidación de sesión guardado
     */
    fun validarSesion(ctx:Context,//v:View,
                      usuarioCi: String,
                      sesion:Sesion,
                      respuesta: RespValidarSesion
    ){
        var sesion_val:String = sesion.sesionVal.toString()
        var token_val:String = sesion.tokenVal.toString()

        val solicitud: Solicitud= Solicitud(
            urlValidarSesion.toString(),
            {
                //val ctx = v.context
                respuesta.respuesta = it.toString()
                println("-RR->"+respuesta.respuesta)
                println("-R->"+respuesta.valida())

                if (respuesta.valida().equals("true")){
                    manejoDeGUI.irAMenuPrincipal(ctx)
                }
                else{
                    manejoDeGUI.irAIniciarSesion(ctx)
                }

            },
            {}
        )
        solicitud.POST(
            "usuario_ci" to usuarioCi,
            "sesion_val" to sesion_val,
            "token_val" to token_val
        )
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