package com.example.systurnomobile.Herramientas

import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.systurnomobile.BDD.ManejoBDD
import com.example.systurnomobile.BDD.Sesion
import com.example.systurnomobile.BDD.Usuario
import com.example.systurnomobile.Herramientas.Respuestas.RespTokenYSesion
import com.example.systurnomobile.Herramientas.Respuestas.RespValidarSesion
import java.net.URL

class ManejoURL(ipServidor: String) {

    //public var ipServidor=""
    private val manejoBDD:ManejoBDD = ManejoBDD()
    private val manejoDeGUI: ManejoDeGUI =
        ManejoDeGUI()

    //private val servidor:String = "http://"+ipServidor+"/SysTurno2020/"
    private val servidor:String = "http://"+ipServidor+"/"

    val urlPruebas: URL = URL(servidor+"pruebajson/")
    val urlLogin:URL = URL(servidor+"login/")
    val urlValidarSesion:URL = URL(servidor+"validarSesion/")
    val urlLogout: URL = URL(servidor+"logout/")

    private val manejoJSON:ManejoJSON = ManejoJSON()


    /**
     * Función de prueba para comprobar acceso a los datos del servidor
     * NO USAR
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
    public fun obtenerToken(v:View,
                            ciUsuario: String,
                            contrasenia:String,
                            panelEspera: RelativeLayout
    ){
        val solicitud:Solicitud = Solicitud(urlLogin.toString(),
            {
                var respuesta:RespTokenYSesion = RespTokenYSesion(it.toString())
                iniciarSesion(
                    v,
                    ciUsuario,
                    contrasenia,
                    respuesta.tokenVal(),
                    respuesta.tokenId()
                )
                //Oculta el panel con la animación de espera
                //TODO: Reemplazar el panel de espera con un DIALOG
                panelEspera.visibility = View.GONE
            },{
                println(it.toString())
            })
        solicitud.POST("usuario_ci" to ciUsuario)
    }


    /**
     * Genera el proceso de iniciar sesión en el servidor
     */
    public fun iniciarSesion(v:View,
                            ciUsuario: String,
                            contrasenia: String,
                             token_val:String,
                             token_id:String
    ){

        val solicitud:Solicitud = Solicitud(urlLogin.toString(),
            {
                val ctx = v.context
                var respuesta: RespTokenYSesion = RespTokenYSesion(it.toString())
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
                    //La aplicación pasa al menú principal
                    manejoDeGUI.irAMenuPrincipal(v)
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
     * Permite analizar la respuesta recibida del servidor
     * al solicitar un TOKEN o una SESION.
     * Devuelve 'false' si la respuesta tiene tipo 'ERROR'
     * de lo contrario devuelve 'true'
     */
    fun analizarRespuesta(respuesta: RespTokenYSesion, ctx: Context):Boolean{
        var resultado:Boolean = false

        if (respuesta.tipo().equals("ERROR")){
            resultado = false
            val adv = manejoDeGUI.mostrarAdvertencia(respuesta.tipo(),respuesta.mensaje(),ctx)
            adv?.show()
        }
        else{
            resultado = true
        }
        return resultado
    }

    /**
     * Solicita al servidor una validación de una sesión guardada en la base local
     * al inicio
     */
    fun validarSesionInicio(ctx:Context,
                      usuarioCi: String,
                      sesion:Sesion
    ){
        var sesion_val:String = sesion.sesionVal.toString()
        var token_val:String = sesion.tokenVal.toString()

        val solicitud: Solicitud= Solicitud(
            urlValidarSesion.toString(),
            {
                var respuesta: RespValidarSesion = RespValidarSesion(it.toString())

                if (respuesta.valida().equals("true")){
                    manejoDeGUI.irAMenuPrincipal(ctx)
                }
                else{
                    manejoDeGUI.irAIniciarSesion(ctx)
                }
            },
            {
                println(it.toString())
            }
        )
        solicitud.POST(
            "usuario_ci" to usuarioCi,
            "sesion_val" to sesion_val,
            "token_val" to token_val
        )
    }

    fun validarSesion(ctx: Context,
                      usuario:Usuario,
                      sesion: Sesion
    ){

        var sesion_val = sesion.sesionVal
        var token_val = sesion.tokenVal
        var usuario_ci = usuario.ci

        val solicitud:Solicitud = Solicitud(
            urlValidarSesion.toString(),
            {

            },
            {

            }
        )
        solicitud.POST()
    }


    //fun cerrarSesion()






}