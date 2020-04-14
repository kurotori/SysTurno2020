package com.example.systurnomobile.Herramientas.Respuestas

class RespTurno(respuesta:String):Respuesta(respuesta) {

    fun id():Int{
        var resp:String = analizarRespuesta("Turno","id","0")
        return resp.toInt()
    }

    fun fechahora():String{
        var resp:String = analizarRespuesta("Turno","fechahora","no_fechahora")
        return resp
    }

    fun estado():String{
        var resp:String = analizarRespuesta("Turno","estado","no_estado")
        return resp
    }
}