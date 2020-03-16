package com.example.systurnomobile.Herramientas

import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

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
        try{
            var datoJSON:JSONObject = JSONObject(stringJson)
            resultado = datoJSON.getJSONObject(objeto).getString(clave)
        }
        catch(e:Exception){
            println(e.message)
        }
        catch(e:JSONException){
            println(e.message)
        }

        return resultado
    }

}