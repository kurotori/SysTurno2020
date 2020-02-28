package com.example.prueba_02

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ServidorAPI {
    @GET("http://192.168.2.59/SysTurno2020/pruebajson/")
    fun leerConEspera():Call<RespuestaServidorPruebas>
}