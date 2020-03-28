package com.example.systurnomobile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceManager
import com.example.systurnomobile.BDD.ManejoBDD
import com.example.systurnomobile.BDD.Sesion
import com.example.systurnomobile.BDD.Usuario
import com.example.systurnomobile.Herramientas.ManejoURL
import com.example.systurnomobile.Herramientas.RespValidarSesion
import com.example.systurnomobile.Herramientas.Solicitud
import kotlinx.android.synthetic.main.activity_pantalla_inicial.*

class PantallaInicial : AppCompatActivity() {

    val manejoBDD = ManejoBDD()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pantalla_inicio)
        //setContentView(R.layout.activity_pantalla_inicial)

        //IMPORTANTE: Se inicializa la clase para manejar las solicitudes
        Solicitud.init(this)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this/*  Activity context */)
        var ipServidor = sharedPreferences.getString("ipServidor","").toString()

        val manejoURL = ManejoURL(ipServidor)

        var sesion: Sesion? = manejoBDD.leerSesion(this)
        var usuario:Usuario? = manejoBDD.leerCiUsuario(this)

        if (sesion == null){
            irAInicioSesion(this)
        }
        else{
            //tv_pinicio_datos.text = manejoBDD.leerSesion(this)?.tokenVal
           // var objRespuesta:RespValidarSesion = RespValidarSesion()
            //manejoURL.validarSesion(this,usuario?.ciUsuario.toString(),sesion,objRespuesta)
        }

    }

    fun irAInicioSesion(ctx:Context){
        val intent: Intent = Intent(ctx,MainActivity::class.java)
        startActivity(intent)
    }
}
