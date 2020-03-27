package com.example.systurnomobile

import android.content.Intent
import android.view.View
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity

class ManejoDeGUI {

    /**
     * Permite cambiar de Layout
     */
    fun cambiarLayout(){

    }

    fun irAMenuPrincipal(v: View){
        val intent: Intent = Intent(v.context,MenuPrincipal::class.java)

    }

}