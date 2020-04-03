package com.example.systurnomobile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.preference.PreferenceManager
import com.example.systurnomobile.BDD.ManejoBDD
import com.example.systurnomobile.Herramientas.ManejoPreferencias
import com.example.systurnomobile.Herramientas.ManejoURL
import com.example.systurnomobile.Herramientas.Respuestas.RespTokenYSesion
import com.example.systurnomobile.Herramientas.Solicitud
import kotlinx.android.synthetic.main.iniciar_sesion.*

import kotlinx.android.synthetic.main.activity_main_original.*

class MainActivity : AppCompatActivity() {
    var ipServidor:String=""
    var manejoURL:ManejoURL? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_original)
        setSupportActionBar(tb_BarraHerramientas)

        //IMPORTANTE: Se inicializa la clase para manejar las solicitudes
        Solicitud.init(this)

        val manejoBDD = ManejoBDD()
        manejoBDD.borrarUsuarios(this)
        manejoBDD.borrarSesiones(this)


        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this/*  Activity context */)
        ipServidor = sharedPreferences.getString("ipServidor","").toString()
        manejoURL = ManejoURL(ipServidor)

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
            R.id.menu_inicio_preferencias -> {
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

    /**
     * Inicio de sesión
     */
    fun iniciarSesion(v: View){
        v.hideKeyboard()
        panelEspera.visibility = View.VISIBLE

        val ciUsuario:String = et_InicioCiUsuario.text.toString()
        val contrasenia:String = et_InicioContrasenia.text.toString()
        //obtenerToken inicia la sesión
        manejoURL?.obtenerToken(v,ciUsuario,contrasenia,panelEspera)
    }

    fun irAMenuPrincipal(v:View){
        val intent: Intent = Intent(v.context,MenuPrincipal::class.java)
        startActivity(intent)
    }

    fun irARegistro(v:View){
        val intent = Intent(v.context,Registro::class.java)
        startActivity(intent)
    }

    fun salir(v:View){
        finishAffinity()
    }
}
