package com.example.systurno_pruebas01

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {

    //lateinit var tv_conexion:TextView
    lateinit var btnIniciarSesion:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chequearConeccionDeRed()
        btnIniciarSesion = findViewById(R.id.btn_iniciar_sesion)

        btnIniciarSesion.setOnClickListener({pasarDatos()})
    }



    private fun chequearConeccionDeRed(): Boolean {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val red = connMgr.activeNetwork
        val networkInfo = connMgr.getNetworkCapabilities(red)//activeNetworkInfo

        val isConnected: Boolean = if(networkInfo != null) networkInfo.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) else false

        if (networkInfo != null && isConnected) {
            // show "Connected" & type of network "WIFI or MOBILE"
            tv_conexion.text = "Conectado"
            // change background color to red
            tv_conexion.setBackgroundColor(-0x8333da)
        } else {
            // show "Not Connected"
            tv_conexion.text = "Not Connected"
            // change background color to green
            tv_conexion.setBackgroundColor(-0x10000)
        }
        return isConnected
    }

    private fun armarJson(): JSONObject {

        val jsonObject = JSONObject()
        jsonObject.accumulate("ci", txt_ci_usuario.getText().toString())

        return jsonObject
    }

    fun pasarDatos(){
        ApiCliente(this)
            .verDatosServidor() { dato, message ->
                if (dato != null) {
                    // TODO: If the user is not empty, then we'll do what is necessary with this object
                    println(dato.dato)
                } else {
                    // TODO: show the user the error message
                    println(message)
                }
            }
        //tv_datos_varios.text = //armarJson().toString()
    }

}

