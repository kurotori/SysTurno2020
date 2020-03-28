package com.example.systurnomobile.Herramientas

class Respuesta() {

    //TODO: Pasar la public var respuesta al parámetro de entrada de la clase, puede ser que deje el código más prolijo

    val manejoJSON:ManejoJSON = ManejoJSON()

    public var respuesta:String=""

    public fun tokenVal():String{
        var resp:String=""
        if(respuesta.length>0){
            resp = manejoJSON.SacarDatoJSON(respuesta,"Token","token_Val")
        }
        else{
            resp = "no_tokenVal"
        }
        return resp
    }

    public fun tokenId():String{
        var resp:String=""
        if(respuesta.length>0){
            resp = manejoJSON.SacarDatoJSON(respuesta,"Token","token_ID")
        }
        else{
            resp = "no_tokenID"
        }
        return resp
    }

    public fun sesionId():String{
        var resp:String=""
        if(respuesta.length>0){
            resp = manejoJSON.SacarDatoJSON(respuesta,"Token","sesion_ID")
        }
        else{
            resp = "no_sesionID"
        }
        return resp
    }

    public fun sesionVal():String{
        var resp:String=""
        if(respuesta.length>0){
            resp = manejoJSON.SacarDatoJSON(respuesta,"Token","sesion_Val")
        }
        else{
            resp = "no_sesionID"
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

    public fun tipo():String{
        var resp:String=""
        if(respuesta.length>0){
            resp = manejoJSON.SacarDatoJSON(respuesta,"Token","tipo")
        }
        else{
            resp = "no_tipo"
        }
        return resp
    }
}