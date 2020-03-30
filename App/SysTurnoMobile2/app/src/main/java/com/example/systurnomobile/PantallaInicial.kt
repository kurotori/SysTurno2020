package com.example.systurnomobile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.preference.PreferenceManager
import com.example.systurnomobile.BDD.ManejoBDD
import com.example.systurnomobile.BDD.Sesion
import com.example.systurnomobile.BDD.Usuario
import com.example.systurnomobile.Herramientas.ManejoURL
import com.example.systurnomobile.Herramientas.Respuestas.RespValidarSesion
import com.example.systurnomobile.Herramientas.Solicitud

class PantallaInicial : AppCompatActivity() {

    val manejoBDD = ManejoBDD()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pantalla_inicio)
        //setSupportActionBar(tb_Inicio_BarraHerramientas)

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
            manejoURL.validarSesionInicio(this,usuario?.ci.toString(),sesion)
            if (usuario != null) {
                println(manejoURL.validarSesion(this,usuario,sesion))
            }
        }

    }

    fun irAInicioSesion(ctx:Context){
        val intent: Intent = Intent(ctx,MainActivity::class.java)
        startActivity(intent)
    }

    //Establece lo que sucede cuando se hace click sobre el boton de preferencias
    //(los tres puntitos)
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.menu_inicio_preferencias-> {
                val intent: Intent = Intent(this, Preferencias::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
