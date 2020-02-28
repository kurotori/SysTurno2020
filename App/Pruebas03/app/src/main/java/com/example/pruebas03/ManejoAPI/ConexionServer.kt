package com.example.pruebas03.ManejoAPI

import android.content.Context
import com.android.volley.Request
import java.net.URL

sealed class ConexionServer {
    val servidor:URL = URL("http://192.168.2.59/SysTurno2020/pruebajson")

    data class DatoPrueba(var ctx:Context):ConexionServer()

    val baseUrl: String
        get() {
            return servidor.toString()
        }

    val espera: Int
        get() {
            return 3000
        }

    val httpMethod: Int
        get() {
            return when (this) {
                is DatoPrueba -> Request.Method.GET
                /*is GetAll -> Request.Method.GET
                is GetCountry -> Request.Method.GET*/
            }
        }
    val params: HashMap<String, String>
        get() {
            return hashMapOf()
//            return when (this) {
//                is GetCountry -> {
//                    hashMapOf(Pair("email", this.email), Pair("password", this.password))
//                }
//                else -> hashMapOf()
//            }
        }

    val headers: HashMap<String, String>
        get() {
            val map: HashMap<String, String> = hashMapOf()
            map["Accept"] = "application/json"
            return  map
//            return when (this) {
//                is GetUser -> {
//                    map["Authorization"] = "Bearer ${UserDefaults(this.ctx).accessToken}"
//                    map
//                }
//                is GetFeature -> {
//                    map["Authorization"] = "Bearer ${UserDefaults(this.ctx).accessToken}"
//                    map
//                }
//                else -> map
//            }
        }

    val url: String
        get() {
            return "$baseUrl/${when (this@ConexionServer) {
                is DatoPrueba -> ""
                }
            }"
        }
}