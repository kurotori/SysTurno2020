package com.example.systurnomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.acerca_de.*

class AcercaDe : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acerca_de)

        wv_visorWeb.loadUrl("file:///android_asset/acercade.html")
    }

    fun volverAMenu(v: View){
        val intent = Intent(v.context, MenuPrincipal::class.java)
        startActivity(intent)
    }
}
