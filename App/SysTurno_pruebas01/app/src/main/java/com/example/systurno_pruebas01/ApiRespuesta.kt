package com.example.systurno_pruebas01

import org.json.JSONObject
import org.json.JSONTokener

/**
 * Esta clase recibe la respuesta del servidor y genera un objeto que
 * que contiene los datos de la misma, categorizados según su tipo, en
 * los atributos públicos del objeto
 * @param respuesta es el string de respuesta recibido desde la API del servidor
 */
class ApiRespuesta(respuesta:String) {

    /**
     * Atributos de la respuesta del servidor:
     * exito: si hubo respuesta o no
     * mensaje: el mensaje a mostrarle al usuario
     * json: el objeto json recibido del servidor
     */
    var exito: Boolean = false
    var mensaje: String = ""
    var json: String = ""

    /**
     * Atributos para manejo interno
     * datos: los datos en bruto del servidor
     * msg: el mensaje de error del servidor
     */
    private val datos = "datos"
    private val msg = "error"

    init { // 'init' corresponde al método constructor en Kotlin
        try {
            /**
             * Interpretamos la respuesta mediante JSONTokener, el cual
             * analiza los datos de la respuesta buscando un objeto JSON
             * para su interpretación
             */
            val jsonToken = JSONTokener(respuesta).nextValue()

            /**
             * Si la respuesta se transformó en un objeto JSON, entonces no hubo un error
             * por el contrario, si no lo es, hubo un error
             */
            if (jsonToken is JSONObject) {
                /**
                 * Transformamos la respuesta en un objeto JSON
                 */
                val jsonRsponse = JSONObject(respuesta)

                /**
                 * Buscamos un mensaje en el JSON
                 */
                mensaje = if (jsonRsponse.has(msg)) {
                    jsonRsponse.getJSONObject(msg).getString("message")
                } else {
                    "An error was occurred while processing the response"
                }

                /**
                 * Analizamos los datos para determinar si hubo exito o no
                 */
                if (jsonRsponse.optJSONObject(datos) != null) {
                    json = jsonRsponse.getJSONObject(datos).toString()
                    exito = true
                } else {
                    exito = false
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}