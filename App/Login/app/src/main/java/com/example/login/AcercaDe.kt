package com.example.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.acerca_de.*
import java.io.File

class AcercaDe : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acerca_de)

        wv_visorWeb.loadUrl("file:///android_asset/acercade.html")
    }

    fun volverAMenu(v: View){
        
        this.finish()
    }
}
