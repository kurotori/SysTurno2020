package com.example.systurnomobile.Herramientas.Respuestas

class RespConfirmacion(respuesta:String):Respuesta(respuesta) {

    fun estado():String{
        var resp:String=analizarRespuesta("Confirmacion","estado","no_estado")
        return resp
    }

    fun mensaje():String{
        var resp:String=analizarRespuesta("Confirmacion","mensaje","no_mensaje")
        return resp
    }
}