package com.example.pruebas03

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.pruebas03.API2.ManejoJSON
import com.example.pruebas03.API2.ManejoURL
import com.example.pruebas03.API2.Solicitud
import com.example.pruebas03.ManejoAPI.ConexionAPI
import com.example.pruebas03.ManejoAPI.ConexionServer
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL
import kotlin.concurrent.thread
import com.example.pruebas03.Funciones.FuncionesGUI
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    var manejoJSON:ManejoJSON = ManejoJSON()
    var manejoURL:ManejoURL = ManejoURL()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Iniciar clase para las solicitudes
        Solicitud.init(this)

        val ctx: Context = applicationContext

    }

    fun resetearCiUsuario(v: View){
        FuncionesGUI.resetearEditText(et_ciUsuario)
    }

    fun bajarToken(v:View){
        var ctx:Context = applicationContext
        var ciUsuario:String = et_ciUsuario.text.toString()
        //manejoURL.leerPrueba(ctx,tv_pruebas)
        manejoURL.obtenerToken(ctx,ciUsuario,tv_pruebas)
    }

}



