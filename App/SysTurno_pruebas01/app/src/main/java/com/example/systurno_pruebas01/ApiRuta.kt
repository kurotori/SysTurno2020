package com.example.systurno_pruebas01

import android.content.Context
import android.content.SharedPreferences
import com.android.volley.Request

sealed class ApiRuta {

    /**
     * Establece el tiempo de espera con el servidor
     */
    val timeOut: Int
        get() {
            return 3000
        }
    /**
     * Establece la ruta hacia el servidor
     */
    val baseUrl: String
        get() {
            return "http://192.168.2.59/SysTurno2020/pruebajson"
        }



    data class Login(var email: String, var password: String, var ctx: Context) : ApiRuta()
    data class GetUser(var ctx: Context) : ApiRuta()
    data class GetFeature(var householdID: Int, var ctx: Context) : ApiRuta()

    data class LeerJsonServidor(var ctx: Context) : ApiRuta()


    /**
     * Para agregar valores extras al URL base
     */
    val url: String
        get() {
            return "$baseUrl/${when (this) {
                else -> ""
            }}"
        }

    /**
     * Establece el método para comunicarse con el servidor
     */
    val httpMethod: Int
        get() {
            return when (this) {
                is Login -> Request.Method.POST
                is GetUser -> Request.Method.GET
                is GetFeature -> Request.Method.GET
                else -> Request.Method.GET
            }
        }

    /**
     * Permite el manejo de parámetros, estableciendo un mapa de valores para manejar
     * en la forma:
     * Pair("nombreDelDato",valorDelDato), Pair(...
     */
    val params: HashMap<String, String>
        get() {
            return when (this) {
                is Login -> {
                    hashMapOf(Pair("email", this.email), Pair("password", this.password))
                }
                else -> hashMapOf()
            }
        }

    /**
     * Permite el manejo de encabezados
     */
    val headers: HashMap<String, String>
        get() {

            val map: HashMap<String, String> = hashMapOf()
            map["Accept"] = "application/json"
            return map
            /*return when (this) {
                is GetUser -> {
                    map["Authorization"] = "Bearer ${}"//"Bearer ${(this.ctx).accessToken}"
                    map
                }
                is GetFeature -> {
                    map["Authorization"] = "Bearer ${UserDefaults(this.ctx).accessToken}"
                    map
                }
                else -> map
                return when (this) {
                    else -> hashMapOf()
                }
            }*/
        }
}

// -------------------------------------------

/*
data class Login(var email: String, var password: String, var ctx: Context) : ApiRuta()
data class GetFeature(var householdID: Int, var ctx: Context) : ApiRuta()
*/
