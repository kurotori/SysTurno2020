package com.example.systurnomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View

class MenuPrincipal : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_principal2)
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
            R.id.menu_usuario_preferencias -> {
                val intent: Intent = Intent(this, Preferencias::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_usuario_cerrarSesion ->{

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
