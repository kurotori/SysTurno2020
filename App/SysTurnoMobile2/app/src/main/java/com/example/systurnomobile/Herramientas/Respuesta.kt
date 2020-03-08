package com.example.systurnomobile.Herramientas

class Respuesta() {

    //TODO: Pasar la public var respuesta al parámetro de entrada de la clase, puede ser que deje el código más prolijo
    //TODO: Agregar una función mensaje() para devolver el mensaje
    val manejoJSON:ManejoJSON = ManejoJSON()

    public var respuesta:String=""

    public fun token():String{
        var resp:String=""
        if(respuesta.length>0){
            resp = manejoJSON.SacarDatoJSON(respuesta,"Token","token")
        }
        else{
            resp = "no_token"
        }
        return resp
    }

    public fun idSesion():String{
        var resp:String=""
        if(respuesta.length>0){
            resp = manejoJSON.SacarDatoJSON(respuesta,"Token","idsesion")
        }
        else{
            resp = "no_idSesion"
        }
        return resp
    }

    public fun mensaje():String{
        var resp:String=""
        if(respuesta.length>0){
            resp = manejoJSON.SacarDatoJSON(respuesta,"Token","mensaje")
        }
        else{
            resp = "no_mensaje"
        }
        return resp
    }
}