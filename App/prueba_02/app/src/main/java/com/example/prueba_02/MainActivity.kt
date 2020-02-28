package com.example.prueba_02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.net.URL
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    val funciones:Funciones = Funciones()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)






       /* val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                println("Response: %s".format(response.toString()))
            },
            Response.ErrorListener { error ->
                // TODO: Handle error
            }
        )*/

        // Access the RequestQueue through your singleton class.
        //MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)


    }

    fun Prueba(v: View){
        tv_dato2.text = funciones.LeerDatoServidorAString()
    }
}
