package com.example.systurnomobile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.systurnomobile.BDD.ManejoBDD
import com.example.systurnomobile.BDD.Sesion
import com.example.systurnomobile.BDD.Usuario
import com.example.systurnomobile.Herramientas.ManejoDeGUI
import com.example.systurnomobile.Herramientas.ManejoURL
import com.example.systurnomobile.Herramientas.Solicitud
import kotlinx.android.synthetic.main.menu_principal2.*
import kotlin.concurrent.thread

class MenuPrincipal : AppCompatActivity() {

    val manejoBDD = ManejoBDD()
    var manejoURL:ManejoURL? = null
    var usuario: Usuario? = null
    var datosUsuario: Usuario?=null
    var sesion: Sesion? = null
    val manejoDeGUI = ManejoDeGUI()
    var ctx: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_principal2)

        ctx = this
        //IMPORTANTE: Se inicializa la clase para manejar las solicitudes
        Solicitud.init(this)

        var cosa = findViewById<TextView>(R.id.tv_solicitarTurno_disponibilidad)

        manejoURL = ManejoURL()
        usuario = manejoBDD.leerCiUsuario(this)
        sesion = manejoBDD.leerSesion(this)

        //Obtenemos los datos del usuario del servidor y los guardamos en la base local
        //y luego los actualizamos en el objeto correspondiente

            if(usuario != null){
                if(sesion != null){
                    val hilo:Thread = thread(start = true) {
                        manejoURL!!.buscarDatosUsuario(this, usuario!!,sesion!!,tv_MenuPrincipalTitulo)

                    }
                    hilo.join()
                    usuario = manejoBDD.leerDatosUsuario(this)
                }
            }

    }


    //Establece lo que sucede cuando se hace click sobre el boton de preferencias
    //(los tres puntitos)
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_usuario, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //Proceso de cerrar sesión
        return when (item.itemId) {
            R.id.menu_usuario_cerrarSesion ->{
                usuario?.let {
                    sesion?.let { it1 ->

                        var dialogo = ctx?.let { it2 ->
                            manejoDeGUI.mostrarDialogoPregunta(
                                "Cerrar Sesión",
                                "Va a cerrar sesión en SysTurno\n¿Desea continuar?",
                                it2,
                                {manejoURL?.cerrarSesion(it2,it,it1)},
                                {})
                        }
                        dialogo?.show()
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    fun nuevaReserva(v: View){
        val intent: Intent = Intent(v.context, SolicitarTurnoRclrMain::class.java)
        manejoURL?.buscarTurnosAbiertos(v.context,usuario!!,sesion!!)
        manejoURL?.buscarMedicamentosRecetadosNoEntIrA(v.context,usuario!!,sesion!!,intent)
    }

    fun cancelarReserva(v:View){
        val intent: Intent = Intent(this, CancelarTurnoRclrMain::class.java)
        manejoURL?.buscarTurnoSolicitado(ctx!!,usuario!!,sesion!!,intent)
    }

    fun acercaDe(v:View){
        val intent = Intent(v.context,AcercaDe::class.java)
        startActivity(intent)
    }

    fun verHistorial(v:View){
        val intent:Intent = Intent(v.context, HistorialRclrMain::class.java)
        manejoURL?.buscarTurnosDelUsuario(v.context,usuario!!,sesion!!,intent)
        //startActivity(intent)
    }

    fun verTelsYDirs(v:View){
        val intent: Intent = Intent(v.context,TelefonosYDirecciones::class.java)
        startActivity(intent)
    }

    fun verRecordatorios(v:View){
        val intent: Intent = Intent(v.context,Recordatorios::class.java)
        startActivity(intent)
    }

    fun salir(v:View){
        manejoDeGUI.mostrarDialogoPregunta(
            "ATENCIÓN",
            "¿Desea salir de la aplicación?",
            v.context,
            {finishAffinity()},
            {}
        )?.show()
    }
}
