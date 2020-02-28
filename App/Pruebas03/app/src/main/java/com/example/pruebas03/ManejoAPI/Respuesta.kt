package com.example.pruebas03.ManejoAPI

import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener

class Respuesta(respuesta:String) {
    var exito:Boolean = false
    var mensaje:String=""
    var json:String=""

    private val data = "data"
    private val msj:String = "error"

    init{
        try{
            val jsonToken = JSONTokener(respuesta).nextValue()
            if (jsonToken is JSONObject){
                val respuestaJson = JSONObject(respuesta)

                mensaje = if(respuestaJson.has(msj)){
                    respuestaJson.getJSONObject(msj).getString("mensaje")
                }else {
                    "ERROR procesando la respuesta"
                }

                if (respuestaJson.optJSONObject(data) != null) {
                    json = respuestaJson.getJSONObject(data).toString()
                    exito = true
                } else {
                    exito = false
                }

            }
            else if (jsonToken is JSONArray) {
                val jsonRsponse = JSONArray(respuesta)
                json = jsonRsponse.toString()
                exito = true
            }

        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }
}