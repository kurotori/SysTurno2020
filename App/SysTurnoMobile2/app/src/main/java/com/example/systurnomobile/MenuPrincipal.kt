package com.example.systurnomobile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.preference.PreferenceManager
import com.example.systurnomobile.BDD.ManejoBDD
import com.example.systurnomobile.BDD.Sesion
import com.example.systurnomobile.BDD.Usuario
import com.example.systurnomobile.Herramientas.ManejoDeGUI
import com.example.systurnomobile.Herramientas.ManejoURL
import com.example.systurnomobile.Herramientas.Solicitud

class MenuPrincipal : AppCompatActivity() {

    val manejoBDD = ManejoBDD()
    var manejoURL:ManejoURL? = null
    var usuario: Usuario? = null
    var sesion: Sesion? = null
    val manejoDeGUI = ManejoDeGUI()
    var ctx: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_principal2)

        ctx = this
        //IMPORTANTE: Se inicializa la clase para manejar las solicitudes
        Solicitud.init(this)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this/*  Activity context */)
        var ipServidor = sharedPreferences.getString("ipServidor","").toString()

        manejoURL = ManejoURL(ipServidor)
        usuario = manejoBDD.leerCiUsuario(this)
        sesion = manejoBDD.leerSesion(this)

    }


    //Establece lo que sucede cuando se hace click sobre el boton de preferencias
    //(los tres puntitos)
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_usuario, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.menu_usuario_cerrarSesion ->{
                usuario?.let {
                    sesion?.let { it1 ->

                        var dialogo = ctx?.let { it2 ->
                            manejoDeGUI.mostrarDialogoPregunta(
                                "Cerrar Sesión",
                                "Va a cerrar sesión en SysTurno\n¿Desea continuar?",
                                it2,
                                {manejoURL?.cerrarSesion(it2,it,it1)},
                                {})
                        }
                        dialogo?.show()
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    public fun nuevaReserva(v: View){
        //val intent: Intent = Intent(this, ReservarTurno::class.java)
        //startActivity(intent)
    }

    fun cancelarReserva(v:View){
        //val intent: Intent = Intent(this, CancelarTurno::class.java)
        //startActivity(intent)
    }

    fun acercaDe(v:View){
        //val intent: Intent= Intent(v.context,AcercaDe::class.java)
        //startActivity(intent)
    }

    fun verHistorial(v:View){
        //val intent: Intent = Intent(v.context, Historial::class.java)
        //val intent:Intent = Intent(this, HistorialRclrMain::class.java)
        //startActivity(intent)
    }

    fun verTelsYDirs(v:View){
        //val intent: Intent = Intent(v.context,DirYTel::class.java)
        //startActivity(intent)
    }

    fun recordatiorios(v:View){
        //val intent: Intent = Intent(v.context,Recordatorios::class.java)
        //startActivity(intent)
    }
}
