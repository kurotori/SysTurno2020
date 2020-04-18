package com.example.systurnomobile.Herramientas.Respuestas

import com.example.systurnomobile.Herramientas.ManejoJSON

/**
 * Esta clase permite manejar e interpretar las respuestas del servidor
 * Las demás clases dentro del paquete 'Respuestas' heredan de esta clase
 * @param respServidor: la respuesta del servidor
 */
open class Respuesta(respServidor: String?) {

    val manejoJSON: ManejoJSON = ManejoJSON()
    var respuesta:String = ""

    init {
        respuesta = respServidor.toString()
    }

    /**
     * Permite analizar la respuesta, extrayendo un dato identificado
     * por una clave específica en la misma o devolviendo un mensaje de error
     * @param nombreObjeto: el nombre del objeto JSON en la respuesta
     * @param clave: la clave que identifica al dato requerido
     * @param respError: el mensaje de error a devolver si no se encuentra el dato solicitado
     */
    fun analizarRespuesta(nombreObjeto:String,clave:String,respError:String):String{
        var resultado:String=""
        if(respuesta.length>0) {
            resultado = manejoJSON.SacarDatoJSON(respuesta,nombreObjeto,clave)
        }
        else {
            resultado = respError
        }
        return resultado
    }



}