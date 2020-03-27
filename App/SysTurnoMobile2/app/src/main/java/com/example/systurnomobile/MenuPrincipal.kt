package com.example.systurnomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MenuPrincipal : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_principal2)
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
