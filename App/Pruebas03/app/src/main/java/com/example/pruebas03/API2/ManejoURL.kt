package com.example.pruebas03.API2

import android.content.Context
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
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

class ManejoURL {



    private val servidor:String = "http://192.168.1.10/SysTurno2020/"

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

    public fun obtenerToken(ctx:Context, ciUsuario: String, tv_destino: TextView){

        val solicitud:Solicitud = Solicitud(urlLogin.toString(),
            {
            tv_destino.text = manejoJSON.SacarDatoJSON(it.toString(),"Token","token")
        },{
                tv_destino.text = it.toString()
            })
        solicitud.POST("ci_usuario" to ciUsuario)
    }



}