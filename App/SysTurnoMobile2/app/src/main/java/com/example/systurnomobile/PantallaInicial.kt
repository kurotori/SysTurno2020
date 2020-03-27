package com.example.systurnomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.systurnomobile.BDD.ManejoBDD
import com.example.systurnomobile.BDD.Sesion
import com.example.systurnomobile.Herramientas.Solicitud
import kotlinx.android.synthetic.main.activity_pantalla_inicial.*

class PantallaInicial : AppCompatActivity() {

    val manejoBDD = ManejoBDD()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_inicial)

        //IMPORTANTE: Se inicializa la clase para manejar las solicitudes
        Solicitud.init(this)

        var sesion: Sesion? = manejoBDD.leerSesion(this)

        if (sesion == null){
            val intent: Intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        else{
            tv_pinicio_datos.text = manejoBDD.leerSesion(this)?.tokenVal
        }

    }

    fun irAInicioSesion(v: View){
        val intent: Intent = Intent(v.context,MainActivity::class.java)
        startActivity(intent)
    }
}
