package com.example.prueba_02

import android.provider.Contacts
import android.widget.TextView
import java.net.URL
import kotlin.concurrent.thread
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.lang.Exception
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


class Funciones:CoroutineScope by MainScope() {

    private val job = Job()
    override val coroutineContext = job + Main

    private val servidor:URL = URL("http://192.168.2.59/SysTurno2020/pruebajson/")

    public fun LeerDatoServidor(tv_destino:TextView){
        val hilo:Thread = thread(start = true){
            val dato_Servidor : String = servidor.readText()
            //println(dato_Servidor)
            tv_destino.text=dato_Servidor
        }
    }

    public fun LeerDatoServidorAString():String{
        var resultado:String="SSSS"
        
        return resultado
    }

   /* fab.setOnClickListener {
        launch(UI) {
            var data = ""
            async(CommonPool) { data = downloadDataBlocking() }.await()
            // process the data on the UI thread
            textView.text = data
        }
    }*/

    suspend fun DatoServer():String{
        return servidor.readText()
    }

    public fun VerDatoServer():String = runBlocking{
        DatoServer()
    }
    /*private fun downloadDataBlocking(): String {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://jsonplaceholder.typicode.com/posts")
            .build()

        val response = client.newCall(request).execute()
        return response.body()?.string() ?: ""
    }*/


}