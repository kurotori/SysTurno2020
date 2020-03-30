package com.example.systurnomobile.Herramientas.Respuestas

import com.example.systurnomobile.Herramientas.ManejoJSON

/**
 * Esta clase permite manejar e interpretar las respuestas del servidor al
 * solicitar Token y Sesiones del mismo
 */
class RespTokenYSesion(respuesta:String):Respuesta(respuesta){

    public fun tokenVal():String{
        var resp:String=analizarRespuesta("Token","token_Val","no_tokenVal")
        return resp
    }

    public fun tokenId():String{
        var resp:String=analizarRespuesta("Token","token_ID","no_tokenID")
        return resp
    }

    public fun sesionId():String{
        var resp:String=analizarRespuesta("Token","sesion_ID","no_sesionID")
        return resp
    }

    public fun sesionVal():String{
        var resp:String=analizarRespuesta("Token","sesion_Val","no_sesionID")
        return resp
    }

    public fun mensaje():String{
        var resp:String=analizarRespuesta("Token","mensaje","no_mensaje")
        return resp
    }

    public fun tipo():String{
        var resp:String=analizarRespuesta("Token","tipo","no_tipo")
        return resp
    }
}