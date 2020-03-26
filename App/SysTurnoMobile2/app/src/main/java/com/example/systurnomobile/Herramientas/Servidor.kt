package com.example.systurnomobile.Herramientas

import com.android.volley.Request

sealed class Servidor () {

    var ipServidor:String
        get() {
            return ipServidor
        }
        set(ip:String) {
        ipServidor = ip
    }

    private val servidor:String = "http://"+ipServidor+"/SysTurno2020/"

    data class ObtenerToken(val ciUsuario:String):Servidor()
    data class Login(val ciUsuario: String, val contrasenia:String):Servidor()
    data class Registro(val ciUsuario: String,
                   val contrasenia: String,
                   val nombre: String,
                   val apellido: String,
                   val direccion: String,
                   val telefonoFijo: String?,
                   val celular: String): Servidor()
    data class BuscarDato(val termino:String,val categoria:String ):Servidor()
    data class GuardarDato(val dato:String, val categoria:String):Servidor()

    val obtenerURL:String
        get() {
            return when (this){
                is ObtenerToken -> servidor+"obtenerToken"
                is Login -> servidor+"login"
                is Registro -> servidor+"registro"
                is BuscarDato -> servidor+"buscarDato"
                is GuardarDato -> servidor+"guardarDato"
            }
        }

    val metodoHTTP: Int
        get(){
            return when (this){
                is ObtenerToken -> Request.Method.POST
                is Login -> Request.Method.POST
                is Registro -> Request.Method.POST
                is BuscarDato -> Request.Method.POST
                is GuardarDato -> Request.Method.POST
            }
        }




}