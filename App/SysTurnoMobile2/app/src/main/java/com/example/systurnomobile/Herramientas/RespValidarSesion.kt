package com.example.systurnomobile.Herramientas

class RespValidarSesion {

    val manejoJSON:ManejoJSON = ManejoJSON()

    public var respuesta:String=""

    fun valida():String{
        var resp:String=""
        if(respuesta.length>0){
            resp = manejoJSON.SacarDatoJSON(respuesta,"SesionValida","valida")
        }
        else{
            resp = "no_respuesta"
        }
        return resp

    }
}