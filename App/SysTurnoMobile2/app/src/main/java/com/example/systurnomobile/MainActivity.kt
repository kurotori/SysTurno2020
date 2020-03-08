package com.example.systurnomobile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.replace
import androidx.preference.PreferenceManager
import com.example.systurnomobile.Herramientas.ManejoPreferencias
import com.example.systurnomobile.Herramientas.ManejoURL
import com.example.systurnomobile.Herramientas.Respuesta
import com.example.systurnomobile.Herramientas.Solicitud

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.inicio.*

class MainActivity : AppCompatActivity() {
    //var servidor: String = ""
    var ipServidor:String=""
    //var manejoURL:ManejoURL = ManejoURL()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //IMPORTANTE: Se inicializa la clase para manejar las solicitudes
        Solicitud.init(this)

        //fab se refiere al botón flotante en la interfáz
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this/*  Activity context */)
        ipServidor = sharedPreferences.getString("ipServidor","").toString()



        val etSalida:EditText = findViewById(R.id.etSalida)
        etSalida.setText(ipServidor)

    }

    //Establece lo que sucede cuando se hace click sobre el boton de preferencias
    //(los tres puntitos)
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent: Intent = Intent(this, Preferencias::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /***
     * Función para ocultar el teclado. Se invoca desde el objeto v: View de la función
     */
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun iniciarSesion(v: View){
        v.hideKeyboard()
        ipServidor = ManejoPreferencias(this).obtenerServidor()
        val ciUsuario:String = etCiUsuario.text.toString()
        var objRespuesta:Respuesta = Respuesta()

        val manejoURL: ManejoURL = ManejoURL(ipServidor)
        //manejoURL.ipServidor = ipServidor
        //manejoURL.obtenerToken(this,ciUsuario,etSalida,objRespuesta)
        manejoURL.iniciarSesion(this,ciUsuario,"",etSalida,objRespuesta)


    }
}
