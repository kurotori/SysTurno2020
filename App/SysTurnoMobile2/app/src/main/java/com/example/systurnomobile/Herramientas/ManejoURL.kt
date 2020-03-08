package com.example.systurnomobile.Herramientas

import android.content.Context
import android.widget.TextView
import androidx.preference.PreferenceManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.systurnomobile.BDD.BaseDeDatos
import com.example.systurnomobile.BDD.Sesion
import com.example.systurnomobile.BDD.SesionDAO
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
import kotlin.coroutines.*

class ManejoURL(ipServidor: String) {

    //public var ipServidor=""
    private var bdd: BaseDeDatos? = null
    private var sesionDAO: SesionDAO? = null

    private val servidor:String = "http://"+ipServidor+"/SysTurno2020/"

    public val urlPruebas: URL = URL(servidor+"pruebajson/")
    public val urlLogin:URL = URL(servidor+"login/")
    private val manejoJSON:ManejoJSON = ManejoJSON()



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

   public fun obtenerToken(ctx:Context,
                            ciUsuario: String,
                            tv_destino: TextView,
                            respuesta: Respuesta){
        val solicitud:Solicitud = Solicitud(urlLogin.toString(),
            {
                tv_destino.text = manejoJSON.SacarDatoJSON(it.toString(),"Token","token")
                respuesta.respuesta = it.toString()
                println(respuesta.token())

            },{
                tv_destino.text = it.toString()
            })
        solicitud.POST("ci_usuario" to ciUsuario)
    }

    public fun iniciarSesion(ctx:Context,
                            ciUsuario: String,
                            contrasenia:String,
                            tv_destino: TextView, //TODO: Retirar el parámetro tv_destino
                            respuesta: Respuesta){

        val solicitud:Solicitud = Solicitud(urlLogin.toString(),
            {
                tv_destino.text = manejoJSON.SacarDatoJSON(it.toString(),"Token","token")
                respuesta.respuesta = it.toString()
                println(respuesta.token())
                //Guardado de datos en la BDD
                bdd = BaseDeDatos.obtenerBDD(ctx)
                sesionDAO = bdd?.sesionDao()

                var sesion = Sesion(
                    null,
                    respuesta.token(),
                    respuesta.idSesion(),
                    respuesta.mensaje())

                with(sesionDAO){
                    this?.nuevaSesion(sesion)
                }

                var cosas = bdd?.sesionDao()?.ultimaSesion()
                cosas?.forEach {
                    tv_destino.setText("token: "+it.token+"\n")
                    tv_destino.append("mensaje: "+it.mensaje)
                    tv_destino.append("id: "+it.id)
                }

            },{
                tv_destino.text = it.toString()
            })
        solicitud.POST("ci_usuario" to ciUsuario,"contrasenia" to contrasenia)
    }



}