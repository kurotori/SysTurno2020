package com.example.systurnomobile.BDD

import android.content.Context
import com.example.systurnomobile.Herramientas.Respuesta
import kotlin.concurrent.thread


class ManejoBDD {

    private var bdd: BaseDeDatos? = null
    private var sesionDAO: SesionDAO? = null

    /**
     * Funcion de pruebas para guardar datos recibidos del servidor y leerlos
     * y corroborar así el correcto funcionamiento de la base local
     */
    public fun guardarLeer(ctx:Context,respuesta:Respuesta):String{
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

    fun guardarDatos(ctx:Context,respuesta: Respuesta){
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
}