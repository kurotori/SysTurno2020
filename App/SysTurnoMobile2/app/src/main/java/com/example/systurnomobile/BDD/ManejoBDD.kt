package com.example.systurnomobile.BDD

import android.content.Context
import com.example.systurnomobile.Herramientas.Respuestas.RespTokenYSesion
import kotlin.concurrent.thread


class ManejoBDD {

    private var bdd: BaseDeDatos? = null
    private var sesionDAO: SesionDAO? = null
    private var usuarioDAO: UsuarioDAO? = null

    /**
     * Funcion de pruebas para guardar datos recibidos del servidor y leerlos
     * y corroborar así el correcto funcionamiento de la base local
     */
    public fun guardarLeer(ctx:Context,respuesta: RespTokenYSesion):String{
        var datos= ""

        val thread:Thread = thread(start = true){
            //Guardado de datos en la BDD
            bdd = BaseDeDatos.obtenerBDD(ctx)
            sesionDAO = bdd?.sesionDao()

            var sesion = Sesion(
                null,
                respuesta.tokenVal(),
                respuesta.tokenId(),
                respuesta.sesionVal(),
                respuesta.sesionId(),
                respuesta.mensaje()
            )

            with(sesionDAO){
                this?.nuevaSesion(sesion)
            }
            //Lectura de datos de la BdD
            var cosas = bdd?.sesionDao()?.ultimaSesion()
            cosas?.forEach {
                datos="tokenVal: "+it.tokenVal
                datos=datos + "\nsesionVal: "+it.sesionVal
                datos=datos + "\nmensaje: "+it.mensaje
                datos = datos + "\nid: "+it.id
            }

        }
        thread.join()
        //println(datos)
        return datos
    }

    /**
     * Crea un usuario en a base local, pero solo almacena su CI
     */
    fun guardarCiUsuario(ctx:Context,ciUsuario:Int){
        val thread:Thread = thread(start = true){
            bdd = BaseDeDatos.obtenerBDD(ctx)
            usuarioDAO = bdd?.usuarioDao()

            var usuario = Usuario(
                ciUsuario
            )
            with(usuarioDAO){
                this?.nuevoUsuario(usuario)
            }
        }
    }

    fun guardarSesion(ctx:Context,respuesta: RespTokenYSesion){
        val thread:Thread = thread(start = true){
            //Guardado de datos en la BDD
            bdd = BaseDeDatos.obtenerBDD(ctx)
            sesionDAO = bdd?.sesionDao()

            var sesion = Sesion(
                null,
                respuesta.tokenVal(),
                respuesta.tokenId(),
                respuesta.sesionVal(),
                respuesta.sesionId(),
                respuesta.mensaje()
            )

            with(sesionDAO){
                this?.nuevaSesion(sesion)
            }
        }
    }

    /**
     * Lee la última sesión almacenada en la base de datos local
     */
    fun leerSesion(ctx: Context): Sesion? {
        var sesion:Sesion? = null

        val thread:Thread = thread(start = true){
            bdd = BaseDeDatos.obtenerBDD(ctx)
            sesionDAO = bdd?.sesionDao()

            var sesiones = bdd?.sesionDao()?.ultimaSesion()

            sesiones?.forEach {
                var ses:Sesion = Sesion(
                    id = it.id,
                    tokenId = it.tokenId,
                    tokenVal = it.tokenVal,
                    sesionId = it.sesionId,
                    sesionVal = it.sesionVal,
                    mensaje = it.mensaje
                )
                println("G:token ID:"+it.tokenId)
                println("G:token Val:"+it.tokenVal)
                println("G:sesion ID:"+it.sesionId)
                println("G:sesion Val:"+it.sesionVal)
                sesion = ses
            }
        }
        //La función join del thread es importante ya que fuerza al sistema a esperar
        // al proceso del thread a que termine
        thread.join()

        println("SG:>"+sesion?.sesionVal)
        return sesion
    }

    /**
     * Obtiene los datos del Usuario de la base de datos local,
     * por ahora solamente el dato de su CI
     */
    fun leerCiUsuario(ctx: Context): Usuario? {
        var usuario:Usuario? = null

        val thread:Thread = thread(start = true){
            bdd = BaseDeDatos.obtenerBDD(ctx)
            usuarioDAO = bdd?.usuarioDao()

            var usuarios = bdd?.usuarioDao()?.verDatosUsuario()

            usuarios?.forEach {
                var usr: Usuario = Usuario(
                    ci = it.ci
                )
                println("ci Usuario:"+it.ci)
                usuario = usr
            }
        }
        //La función join del thread es importante ya que fuerza al sistema a esperar
        // al proceso del thread a que termine
        thread.join()
        return usuario
    }

    /**
     * Borra todos los datos de Usuario de la base local
     */
    fun borrarUsuarios(ctx: Context){
        val thread:Thread = thread(start = true) {
            bdd = BaseDeDatos.obtenerBDD(ctx)
            usuarioDAO = bdd?.usuarioDao()

            usuarioDAO?.borrarTodosLosUsuarios()
        }
        thread.join()
    }

}