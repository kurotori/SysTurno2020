package com.example.systurnomobile.Herramientas.Respuestas

import com.example.systurnomobile.Herramientas.ManejoJSON

class RespValidarSesion(respuesta:String):Respuesta(respuesta) {

    fun valida():String{
        var resp:String=analizarRespuesta("SesionValida","valida","no_respuesta")
        return resp
    }
}