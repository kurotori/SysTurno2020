package com.example.systurnomobile.Herramientas

import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.systurnomobile.BDD.ManejoBDD
import com.example.systurnomobile.BDD.Sesion
import com.example.systurnomobile.BDD.Usuario
import com.example.systurnomobile.Fragmentos.DialogoEspera
import com.example.systurnomobile.Herramientas.Respuestas.*
import java.net.URL

class ManejoURL(ipServidor: String) {

    //public var ipServidor=""
    private val manejoBDD:ManejoBDD = ManejoBDD()
    private val manejoDeGUI: ManejoDeGUI =
        ManejoDeGUI()

    //private val servidor:String = "http://"+ipServidor+"/SysTurno2020/"
    private val servidor:String = "https://"+ipServidor+"/"

    val urlPruebas: URL = URL(servidor+"pruebajson/")
    val urlLogin:URL = URL(servidor+"login/")
    val urlValidarSesion:URL = URL(servidor+"validarSesion/")
    val urlLogout: URL = URL(servidor+"logout/")
    val urlBuscar: URL = URL(servidor+"buscar/")
    val urlRegistro: URL = URL(servidor+"registro/")

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
    public fun iniciarSesion(v:View,
                            ciUsuario: String,
                            contrasenia:String
    ){
        val ctx = v.context
        //Se muestra el diálogo de espera
        var dialogo = manejoDeGUI.mostrarDialogoEspera(ctx)
        dialogo?.show()
        //1era Solicitud: token
        val solicitud:Solicitud = Solicitud(urlLogin.toString(),
            {
                var respuesta:RespTokenYSesion = RespTokenYSesion(it.toString())
                //2da solicitud: inicio de sesión
                val solicitudIS = Solicitud(
                    urlLogin.toString(),
                    {
                        val ctx = v.context
                        var respuesta: RespTokenYSesion = RespTokenYSesion(it.toString())
                        println("tipo: "+respuesta.tipo())
                        println("mensaje: "+respuesta.mensaje())
                        println("token ID: "+respuesta.tokenId())
                        println("token Val: "+respuesta.tokenVal())
                        println("sesion ID: "+respuesta.sesionId())
                        println("sesion Val: "+respuesta.sesionVal())
                        //Oculta el panel con la animación de espera
                        dialogo?.dismiss()
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
                    },
                    {

                    }
                )
                solicitudIS.POST("usuario_ci" to ciUsuario,
                    "contrasenia" to contrasenia,
                    "token_val" to respuesta.tokenVal(),
                    "token_id" to respuesta.tokenId())
            },{
                println(it.toString())
            })
        solicitud.POST("usuario_ci" to ciUsuario)
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
     * al iniciar_sesion
     */
    fun validarSesionInicio(ctx:Context,
                      usuario:Usuario,
                      sesion:Sesion
    ){
        var usuario_ci = usuario.ci
        var sesion_val:String = sesion.sesionVal.toString()
        var token_val:String = sesion.tokenVal.toString()

        val solicitud: Solicitud= Solicitud(
            urlValidarSesion.toString(),
            {
                var respuesta: RespValidarSesion = RespValidarSesion(it.toString())
                println("Estado de la sesion:"+respuesta.valida())
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
            "usuario_ci" to usuario_ci,
            "sesion_val" to sesion_val,
            "token_val" to token_val
        )
    }

    fun validarAccion(ctx: Context,
                      usuario:Usuario,
                      sesion: Sesion,
                      accion:()->Unit
    ){

        var sesion_val = sesion.sesionVal
        var token_val = sesion.tokenVal
        var usuario_ci = usuario.ci

        val solicitud = Solicitud(
            urlValidarSesion.toString(),
            {
                var respuesta = RespValidarSesion(it.toString())
                if(respuesta.valida().equals("true")){
                    accion()
                }
                else{
                    manejoDeGUI.mostrarAdvertencia("ERROR","Error Interno de la app",ctx)
                }
            },
            {
                println(it.toString())
            }
        )
        solicitud.POST(
            "usuario_ci" to usuario_ci,
            "sesion_val" to sesion_val.toString(),
            "token_val" to token_val.toString()
        )
    }

    /**
     * Inicia el proceso de cerrar sesiones en el sistema
     */
    fun cerrarSesion(ctx: Context,
                     usuario: Usuario,
                     sesion: Sesion
    ){
        var sesion_val = sesion.sesionVal
        var token_val = sesion.tokenVal
        var usuario_ci = usuario.ci
        //Se muestra el diálogo de espera
        var dialEsp = manejoDeGUI.mostrarDialogoEspera(ctx)
        dialEsp?.show()
        //Se valida el proceso con el servidor
        validarAccion(ctx,usuario,sesion
        ) {
            //Se inicia la solicitud al servidor
            val solicitud = Solicitud(
                urlLogout.toString(),
                {
                    var respuesta = RespLogout(it.toString())
                    dialEsp?.dismiss()
                    var dialogo =
                        manejoDeGUI.mostrarAdvertencia("Notificación",respuesta.mensaje(),ctx){
                            manejoDeGUI.irAIniciarSesion(ctx)
                        }
                    if(respuesta.estado().equals("OK")){
                        dialogo?.show()
                    }
                    else{
                        dialogo?.show()
                    }
                },
                {
                    println(it.toString())
                })
            solicitud.POST(
                "usuario_ci" to usuario_ci,
                "sesion_val" to sesion_val.toString(),
                "token_val" to token_val.toString()
            )
        }
    }


    /***
     * Permite iniciar el proceso de registrar a un usuario en el sistema
     */
    fun registrarUsuario(ctx: Context,
                         usuario: Usuario,
                         contrasenia:String
                         ){
        var usuario_ci = usuario.ci.toString()
        var nombre = usuario.nombre
        var apellido = usuario.apellido
        var direccion = usuario.direccion
        var telefono = usuario.telefono
        var email = usuario.email

        //Se muestra el diálogo de espera
        var dialogo = manejoDeGUI.mostrarDialogoEspera(ctx)
        dialogo?.show()
        //1era solicitud: token
        val solicitud = Solicitud(
            urlRegistro.toString(),
            {
                var respuesta:RespTokenYSesion = RespTokenYSesion(it.toString())
                var token_val = respuesta.tokenVal()
                var token_id = respuesta.tokenId()

                //2da solicitud: registro
                val solicitud = Solicitud(
                    urlRegistro.toString(),
                    {
                        var respuesta:RespRegistro = RespRegistro(it.toString())
                        dialogo?.dismiss()
                        manejoDeGUI.mostrarAdvertencia(respuesta.estado(),respuesta.mensaje(),ctx,{
                            manejoDeGUI.irAIniciarSesion(ctx)
                        })?.show()

                    },
                    {}
                )
                solicitud.POST(
                    "usuario_ci" to usuario_ci,
                    "token_val" to token_val,
                    "token_id" to token_id,
                    "nombre" to nombre.toString(),
                    "apellido" to apellido.toString(),
                    "direccion" to direccion.toString(),
                    "telefono" to telefono.toString(),
                    "email" to email.toString(),
                    "contrasenia" to contrasenia
                )
            },
            {

            }
        )
        solicitud.POST(
            "usuario_ci" to usuario_ci
        )

    }

    fun buscarDatosUsuario(ctx: Context,
                         usuario: Usuario,
                         sesion: Sesion){
        var sesion_val = sesion.sesionVal
        var token_val = sesion.tokenVal
        var usuario_ci = usuario.ci
        //1 - Validación
        validarAccion(ctx,usuario,sesion){
            //2 - Solicitud de datos
            val solicitud = Solicitud(
                urlBuscar.toString(),
                {
                    var respuesta = RespDatosUsuario(it.toString())
                    var datosUsuario = Usuario(
                        ci = respuesta.usuario_ci().toInt(),
                        nombre = respuesta.nombre(),
                        apellido = respuesta.apellido(),
                        direccion = respuesta.direccion(),
                        email = respuesta.email(),
                        telefono = respuesta.telefono(),
                        recibe_email = respuesta.recibeEmail(),
                        recibe_sms = respuesta.recibeSMS()
                    )
                    manejoBDD.guardarDatosUsuario(ctx, datosUsuario)
                },
                {

                }
            )
            solicitud.POST(
                "tipo" to "10",
                "usuario_ci" to usuario_ci,
                "token_val" to token_val.toString(),
                "sesion_val" to sesion_val.toString()
            )
        }

    }






}