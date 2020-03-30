package com.example.systurnomobile.Herramientas

import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

class ManejoJSON {

    /**
     * Función de pruebas para comprobar la funcionalidad
     * NO USAR
     */
    public fun RecibirString(dato:String):String{
        return "Recibi esto: " + dato
    }

    /**
     * Función de pruebas para comprobar la funcionalidad
     * NO USAR
     */
    public fun SacarDatoJSON(stringJson:String):String{
        val gson:Gson = Gson()
        var resultado:String =""
        var datoJSON:JSONObject = JSONObject(stringJson)
        resultado = datoJSON.getJSONObject("Pruebas").getString("dato")
        return resultado
    }

    /**
     * Permite extraer un dato específico de un objeto JSON, buscando mediante su clave
     */
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