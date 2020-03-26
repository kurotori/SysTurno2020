package com.example.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Recordatorios : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recordatorio)


    }

    fun volverAMenu(v: View){
        this.finish()
    }
}
