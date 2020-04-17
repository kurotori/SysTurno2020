package com.example.systurnomobile.Herramientas

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.systurnomobile.BDD.*
import com.example.systurnomobile.Fragmentos.DialogoEspera
import com.example.systurnomobile.Herramientas.Respuestas.*
import com.example.systurnomobile.R
import java.net.URL

class ManejoURL() {

    private val manejoBDD:ManejoBDD = ManejoBDD()
    private val manejoDeGUI: ManejoDeGUI = ManejoDeGUI()

    private val servidor:String = "https://systurno-movil.000webhostapp.com/"

    val urlPruebas: URL = URL(servidor+"pruebajson/")
    val urlLogin:URL = URL(servidor+"login/")
    val urlValidarSesion:URL = URL(servidor+"validarSesion/")
    val urlLogout: URL = URL(servidor+"logout/")
    val urlBuscar: URL = URL(servidor+"buscar/")
    val urlRegistro: URL = URL(servidor+"registro/")
    val urlActualizar: URL = URL(servidor + "actualizar/")
    val urlTurnos:URL = URL(servidor+"turnos/")

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
                            usuario_ci: String,
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
                            //var ciUsr: Int = ciUsuario.toInt()
                            manejoBDD.guardarCiUsuario(ctx,usuario_ci.toInt())
                            var sesion = manejoBDD.leerSesion(ctx)
                            var usuario = Usuario(ci = usuario_ci.toInt())
                            if(sesion!=null){
                                buscarDatosUsuario(ctx,usuario,sesion,null)
                            }

                            //La aplicación pasa al menú principal
                            manejoDeGUI.irAMenuPrincipal(v)
                        }
                    },
                    {

                    }
                )
                solicitudIS.POST("usuario_ci" to usuario_ci,
                    "contrasenia" to contrasenia,
                    "token_val" to respuesta.tokenVal(),
                    "token_id" to respuesta.tokenId())
            },{
                println(it.toString())
            })
        solicitud.POST("usuario_ci" to usuario_ci)
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

    /**
     * Busca los datos del usuario en el servidor. Si hay un TextView indicado, actualiza
     * el texto de ese textView
     */
    fun buscarDatosUsuario(ctx: Context,
                           usuario: Usuario,
                           sesion: Sesion,
                           titulo: TextView?
    ){
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
                    println("Encontrado:"+respuesta.nombre()+" "+respuesta.apellido())

                    if(titulo!=null){
                        titulo.text = ctx.getString(
                            R.string.menu_principal_tv_titulo,
                            respuesta.nombre(),
                            respuesta.apellido()
                        )
                    }
                    val usr=Usuario(
                        ci = respuesta.usuario_ci().toInt(),
                        nombre = respuesta.nombre(),
                        apellido = respuesta.apellido(),
                        direccion = respuesta.direccion(),
                        telefono = respuesta.telefono(),
                        email = respuesta.email(),
                        recibe_sms = respuesta.recibeSMS(),
                        recibe_email = respuesta.recibeEmail()
                    )
                    //Una vez se obtienen del servidor, los datos se almacenan en la base local
                    manejoBDD.actualizarDatosUsuario(ctx,usr)
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


    /**
     * Actualiza los datos del perfil del usuario
     */
    fun actualizarDatos(ctx:Context,
                        datosUsuario:Usuario,
                        sesion: Sesion,
                        tipo: Int
    ){
        //Se muestra el diálogo de espera
        var dialogo = manejoDeGUI.mostrarDialogoEspera(ctx)
        dialogo?.show()
        //1 - Validación
        validarAccion(ctx,datosUsuario,sesion){
            println("validación ok")
            println("Desde actualizarDatos: CI Recibida: "+datosUsuario.ci)
            println("Desde actualizarDatos: Sesion Recibida: "+sesion.sesionVal)
            println("Desde actualizarDatos: Token Recibido: "+sesion.tokenVal)
            //2 - Actualización de datos
            val solicitud = Solicitud(
                urlActualizar.toString(),
                {
                    var respuesta=RespConfirmacion(it.toString())
                    manejoBDD.actualizarDatosUsuario(ctx,datosUsuario)
                    dialogo?.dismiss()
                    manejoDeGUI.mostrarAdvertencia(respuesta.estado(),respuesta.mensaje(),ctx)?.show()
                },
                {
                    println("ERROR:respuesta:"+it)
                }
            )
            solicitud.POST(
                "usuario_ci" to datosUsuario.ci,
                "token_val" to sesion.tokenVal.toString(),
                "sesion_val" to sesion.sesionVal.toString(),
                "tipo" to tipo.toString(),
                "nombre" to datosUsuario.nombre.toString(),
                "apellido" to datosUsuario.apellido.toString(),
                "direccion" to datosUsuario.direccion.toString(),
                "telefono" to datosUsuario.telefono.toString(),
                "email" to datosUsuario.email.toString(),
                "recibeMail" to datosUsuario.recibe_email.toString(),
                "recibeSMS" to datosUsuario.recibe_sms.toString()
            )
        }
    }

    fun buscarMedicamentosRecetadosNoEnt(ctx:Context,
                                         usuario:Usuario,
                                         sesion: Sesion
    ){
        //Se muestra el diálogo de espera
        var dialogo = manejoDeGUI.mostrarDialogoEspera(ctx)
        dialogo?.show()
        //1 - Validamos la acción
        validarAccion(ctx,usuario,sesion){
            // 2- Generamos la solicitud de datos
            val solicitud = Solicitud(
                servidor+"prueba_busqueda.php",
                {
                    println("manejando la respuesta del servidor")
                    var respuesta = RespMedicamentosRecetados(it.toString())
                    var meds = respuesta.listaMeds()
                    meds?.forEach {
                        manejoBDD.guardarMedicamentosRecetados(ctx,it)
                    }
                    dialogo?.dismiss()
                },
                {
                    println("ERROR:"+it)
                }
            )
            solicitud.POST()
        }
    }

    /**
     * Busca los medicamentos recetados y abre otra actividad
     */
    fun buscarMedicamentosRecetadosNoEntIrA(ctx:Context,
                                         usuario:Usuario,
                                         sesion: Sesion,
                                         intent: Intent
    )
    {
        //Se muestra el diálogo de espera
        var dialogo = manejoDeGUI.mostrarDialogoEspera(ctx)
        dialogo?.show()
        //1 - Validamos la acción
        validarAccion(ctx,usuario,sesion){
            // 2- Generamos la solicitud de datos
            val solicitud = Solicitud(
                urlBuscar.toString(),
                {
                    println("manejando la respuesta del servidor")
                    var respuesta = RespMedicamentosRecetados(it.toString())
                    var meds = respuesta.listaMeds()
                    println("Longitud de lista de meds:"+meds?.size)

                    manejoBDD.borrarMedicamentos(ctx)
                    meds?.forEach {
                        manejoBDD.guardarMedicamentosRecetados(ctx,it)
                    }
                    dialogo?.dismiss()
                    ContextCompat.startActivity(ctx,intent,null)
                },
                {
                    println("ERROR:"+it)
                }
            )
            solicitud.POST(
                "usuario_ci" to usuario.ci,
                "token_val" to sesion.tokenVal.toString(),
                "sesion_val" to sesion.sesionVal.toString(),
                "tipo" to "11"
            )
        }
    }

    /**
     * Obtiene el listado de turnos mas reciente disponible en el servidor
     */
    fun buscarTurnosAbiertos(ctx:Context,
                             usuario:Usuario,
                             sesion: Sesion/*,
                                 tv_fecha:TextView,
                                 tv_numero:TextView,
                                 tv_hora:TextView*/
    ){
        //Se muestra el diálogo de espera
        //var dialogo = manejoDeGUI.mostrarDialogoEspera(ctx)
        //dialogo?.show()

        //1 - Validamos la solicitud
        validarAccion(ctx,usuario,sesion){
            //2 - Solicitamos los datos
            val solicitud = Solicitud(
                urlBuscar.toString(),
                {
                    var respuesta:RespTurnos= RespTurnos(it.toString())
                    var turnos = respuesta.listaTurnos()

                    manejoBDD.borrarTurnos(ctx)
                    turnos.forEach {
                        manejoBDD.nuevoTurno(ctx,it)
                    }
                },
                {
                    println("ERROR: " + it)
                }
            )
            solicitud.POST(
                "usuario_ci" to usuario.ci,
                "token_val" to sesion.tokenVal.toString(),
                "sesion_val" to sesion.sesionVal.toString(),
                "tipo" to "21"
            )
        }
    }

    /**
     * Actualiza el listado de turnos
     */
    fun actualizarTurnosAbiertos(ctx:Context,
                                usuario:Usuario,
                                sesion: Sesion
    ){

        //Se muestra el diálogo de espera
        var dialogo = manejoDeGUI.mostrarDialogoEspera(ctx)
        dialogo?.show()

        //1 - Validamos la solicitud
        validarAccion(ctx,usuario,sesion){
            //2 - Solicitamos los datos
            val solicitud = Solicitud(
                urlBuscar.toString(),
                {
                    var respuesta:RespTurnos= RespTurnos(it.toString())
                    var turnos = respuesta.listaTurnos()
                    turnos.forEach {
                        manejoBDD.nuevoTurno(ctx,it)
                    }
                },
                {
                    println("ERROR: " + it)
                }
            )
        }
    }


    /**
     * Inicia el proceso de solicitar un turno
     */
    fun solicitarTurno(ctx:Context,
                       usuario:Usuario,
                       sesion: Sesion,
                       turno_id:String
    ){
        //1 - mostramos el diálogo de espera
        val dialogo = manejoDeGUI.mostrarDialogoEspera(ctx)
        dialogo?.show()
        //2 - Validamos la acción
        validarAccion(ctx,usuario,sesion){
            //3 - Generamos la solicitud
            val solicitud= Solicitud(
                urlTurnos.toString(),
                {
                    var respuesta = RespConfirmacion(it.toString())
                    dialogo?.dismiss()
                    if(respuesta.estado().equals("ERROR")){
                        manejoDeGUI.mostrarAdvertencia("Error",respuesta.mensaje(),ctx)?.show()
                        println(respuesta.estado()+":"+respuesta.mensaje())
                    }
                    else{
                        var d=manejoDeGUI.mostrarAdvertencia("",respuesta.mensaje(),ctx){
                            manejoDeGUI.irAMenuPrincipal(ctx)
                        }
                        d?.show()
                    }
                },
                {
                    println("Error:"+it)
                }
            )
            solicitud.POST(
                "usuario_ci" to usuario.ci,
                "token_val" to sesion.tokenVal.toString(),
                "sesion_val" to sesion.sesionVal.toString(),
                "turno_id" to turno_id,
                "tipo" to "100"
            )
        }
    }

    /**
     * Solicita el listado de turnos de un usuario
     */
    fun buscarTurnosDelUsuario(ctx:Context,
                             usuario:Usuario,
                             sesion: Sesion,
                               intent: Intent
    ){
        //Se muestra el diálogo de espera
        var dialogo = manejoDeGUI.mostrarDialogoEspera(ctx)
        dialogo?.show()

        //1 - Validamos la solicitud
        validarAccion(ctx,usuario,sesion){
            //2 - Solicitamos los datos
            val solicitud = Solicitud(
                urlBuscar.toString(),
                {
                    var respuesta:RespMisTurnos= RespMisTurnos(it.toString())
                    var turnos = respuesta.listaMisTurnos()

                    manejoBDD.borrarMisTurnos(ctx)
                    turnos.forEach {
                        manejoBDD.nuevoTurnoSolicitado(ctx,it)
                    }
                    dialogo?.dismiss()
                    ContextCompat.startActivity(ctx,intent,null)
                },
                {
                    println("ERROR: $it")
                }
            )
            solicitud.POST(
                "usuario_ci" to usuario.ci,
                "token_val" to sesion.tokenVal.toString(),
                "sesion_val" to sesion.sesionVal.toString(),
                "tipo" to "30"
            )
        }
    }

    /**
     * Solicita al servidor todos los datos del turno que haya sido solicitado por el usuario al sistema
     * y pasa a la pantalla  de Cancelación de turno, renovando y guardando todos los datos relacionados.
     * Si no encuentra turnos solicitados muestra una advertencia.
     */
    fun buscarTurnoSolicitado(ctx:Context,
                              usuario:Usuario,
                              sesion: Sesion,
                              intent: Intent
    ){
        //Se muestra el diálogo de espera
        var dialogo = manejoDeGUI.mostrarDialogoEspera(ctx)
        dialogo?.show()
        //1 - Validamos la solicitud
        validarAccion(ctx,usuario,sesion){
            val solicitud = Solicitud(
                urlBuscar.toString(),
                {
                    var respuesta = RespTurnoSolicitado(it.toString())
                    dialogo?.dismiss()
                    if(respuesta.turnoSolicitado().id == 0){
                        manejoDeGUI.mostrarAdvertencia("ATENCIÓN","Usted no tiene ningún turno activo a su nombre en el sistema.",ctx)?.show()
                    }
                    else{
                        manejoBDD.borrarMisTurnos(ctx)
                        manejoBDD.borrarMisRecetas(ctx)
                        manejoBDD.borrarMedicamentos(ctx)

                        manejoBDD.nuevoTurnoSolicitado(ctx,respuesta.turnoSolicitado())
                        var listaRecetas = respuesta.recetasDelTurno()
                        listaRecetas.forEach {
                            manejoBDD.nuevaMiReceta(ctx,it)
                        }
                        respuesta.medicamentosDelTurno().forEach {
                            manejoBDD.guardarMedicamentosRecetados(ctx,it)
                        }
                        ContextCompat.startActivity(ctx,intent,null)
                    }
                },
                {
                    println(it)
                }
            )
            solicitud.POST(
                "usuario_ci" to usuario.ci,
                "token_val" to sesion.tokenVal.toString(),
                "sesion_val" to sesion.sesionVal.toString(),
                "tipo" to "31"
            )
        }
    }

    /**
     * Solicita la cancelación de cualquier turno asignado al usuario.
     */
    fun cancelarTurnoSolicitado(ctx:Context,
                                usuario:Usuario,
                                sesion: Sesion
    ){
        //Se muestra el diálogo de espera
        var dialogo = manejoDeGUI.mostrarDialogoEspera(ctx)
        dialogo?.show()
        //1 - Validamos la solicitud
        validarAccion(ctx,usuario,sesion){
            val solicitud = Solicitud(
                urlTurnos.toString(),
                {
                    var respuesta = RespConfirmacion(it.toString())
                    dialogo?.dismiss()
                    manejoDeGUI.mostrarAdvertencia("",respuesta.mensaje(),ctx) {
                        manejoDeGUI.irAMenuPrincipal(ctx)
                    }?.show()
                },
                {
                    println(it)
                }
            )
            solicitud.POST(
                "usuario_ci" to usuario.ci,
                "token_val" to sesion.tokenVal.toString(),
                "sesion_val" to sesion.sesionVal.toString(),
                "tipo" to "101"
            )
        }
    }
}