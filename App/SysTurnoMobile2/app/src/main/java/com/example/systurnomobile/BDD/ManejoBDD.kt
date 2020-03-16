package com.example.systurnomobile.BDD

import android.content.Context
import com.example.systurnomobile.Herramientas.Respuesta
import kotlin.concurrent.thread


class ManejoBDD {

    private var bdd: BaseDeDatos? = null
    private var sesionDAO: SesionDAO? = null

    public fun guardarLeer(ctx:Context,respuesta:Respuesta):String{
        var datos= ""

        val thread:Thread = thread(start = true){
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
                datos="token: "+it.token
                datos=datos + "\nmensaje: "+it.mensaje
                datos = datos + "\nid: "+it.id
            }

        }
        thread.join()
        println(datos)
        return datos
    }
}