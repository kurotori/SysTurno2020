package com.example.login

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
        val intent: Intent = Intent(this, ReservarTurno::class.java)
        startActivity(intent)
    }

    fun cancelarReserva(v:View){
        val intent: Intent = Intent(this, CancelarTurno::class.java)
        startActivity(intent)
    }

    fun acercaDe(v:View){
        val intent: Intent= Intent(v.context,AcercaDe::class.java)
        startActivity(intent)
    }
}
