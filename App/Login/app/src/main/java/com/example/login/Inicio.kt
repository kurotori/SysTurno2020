package com.example.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Inicio : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    public fun IrARegistro(v: View){
        val intent: Intent = Intent(this, Registro::class.java)
        startActivity(intent)
    }

    public fun IrAMenu(v: View){
        val intent: Intent = Intent(this, MenuPrincipal::class.java)
        startActivity(intent)
    }

}
