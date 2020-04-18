package com.example.systurnomobile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.app.ActivityCompat
import com.example.systurnomobile.Herramientas.ManejoDeGUI
import kotlinx.android.synthetic.main.registro_nuevo.*

class TelefonosYDirecciones : AppCompatActivity() {

    val manejoDeGUI = ManejoDeGUI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dir_y_tel)

    }

    fun llamarA(v: View){
        val btn = findViewById<Button>(v.id)
        val num = btn.text
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+num))
        manejoDeGUI.mostrarDialogoPregunta("ATENCIÓN","¿Llamar al número "+num+"?",v.context,
            {
                startActivity(intent)
            },
            {}
        )?.show()
    }

    fun volverAMenu(v: View){
        val intent = Intent(v.context, MenuPrincipal::class.java)
        startActivity(intent)
    }
}
