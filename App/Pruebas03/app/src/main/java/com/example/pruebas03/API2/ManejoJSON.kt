package com.example.pruebas03.API2

import com.google.gson.Gson
import org.json.JSONObject

class ManejoJSON {

    public fun RecibirString(dato:String):String{
        return "Recibi esto: " + dato
    }

    public fun SacarDatoJSON(stringJson:String):String{
        val gson:Gson = Gson()
        var resultado:String =""
        var datoJSON:JSONObject = JSONObject(stringJson)
        resultado = datoJSON.getJSONObject("Pruebas").getString("dato")
        return resultado
    }

    public fun SacarDatoJSON(stringJson:String, objeto:String, clave:String):String{
        val gson:Gson = Gson()
        var resultado:String =""
        var datoJSON:JSONObject = JSONObject(stringJson)
        resultado = datoJSON.getJSONObject(objeto).getString(clave)
        return resultado
    }

}