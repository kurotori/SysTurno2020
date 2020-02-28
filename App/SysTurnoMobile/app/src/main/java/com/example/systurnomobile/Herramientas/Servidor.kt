package com.example.systurnomobile.Herramientas

sealed class Servidor {
    class ObtenerToken(val ciUsuario:String):Servidor()
    class Login(val ciUsuario: String, val contrasenia:String):Servidor()
    class Registro(val ciUsuario: String,
                   val contrasenia: String,
                   val nombre: String,
                   val apellido: String,
                   val direccion: String,
                   val telefonoFijo: String?,
                   val celular: String): Servidor()
    class BuscarDato(val termino:String,val categoria:String ):Servidor()
    class GuardarDato(val dato:String, val categoria:String):Servidor()
    

}