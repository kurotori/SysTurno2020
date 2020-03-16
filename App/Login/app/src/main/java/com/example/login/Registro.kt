package com.example.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.registro_nuevo.*

class Registro : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_nuevo)

        btn_registroConfirmar.setOnClickListener{view ->
            Snackbar.make(view, "Usuario Creado con Éxito",Snackbar.LENGTH_LONG)
                .setAction("Registrar",null).show()
        }
    }

    public fun volverAMenu(v: View){
        val intent: Intent = Intent(v.context,Inicio::class.java)
        startActivity(intent)
    }
}
