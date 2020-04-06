package com.example.systurnomobile.Herramientas.Respuestas

class RespDatosUsuario(respuesta:String):Respuesta(respuesta) {

    fun usuario_ci():String{
        var resp:String=analizarRespuesta("DatosUsuario","usuario_CI","no_usuario_CI")
        return resp
    }

    fun nombre():String{
        var resp:String=analizarRespuesta(
            "DatosUsuario",
            "nombre",
            "no_nombre")
        return resp
    }

    fun apellido():String{
        var resp:String=analizarRespuesta(
            "DatosUsuario",
            "apellido",
            "no_apellido")
        return resp
    }

    fun telefono():String{
        var resp:String=analizarRespuesta(
            "DatosUsuario",
            "telefono",
            "no_telefono")
        return resp
    }

    fun email():String{
        var resp:String=analizarRespuesta(
            "DatosUsuario",
            "email",
            "no_email")
        return resp
    }

    fun direccion():String{
        var resp:String=analizarRespuesta(
            "DatosUsuario",
            "direccion",
            "no_direccion")
        return resp
    }

    fun recibeSMS():String{
        var resp:String=analizarRespuesta(
            "DatosUsuario",
            "recibeSMS",
            "no_prefSMS")
        return resp
    }

    fun recibeEmail():String{
        var resp:String=analizarRespuesta(
            "DatosUsuario",
            "recibeEmail",
            "no_prefEmail")
        return resp
    }
}