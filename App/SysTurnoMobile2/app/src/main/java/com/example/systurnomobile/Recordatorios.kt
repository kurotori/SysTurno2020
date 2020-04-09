package com.example.systurnomobile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.example.systurnomobile.BDD.ManejoBDD
import com.example.systurnomobile.BDD.Sesion
import com.example.systurnomobile.BDD.Usuario
import com.example.systurnomobile.Herramientas.ManejoDeGUI
import com.example.systurnomobile.Herramientas.ManejoURL
import com.example.systurnomobile.Herramientas.Solicitud
import kotlinx.android.synthetic.main.recordatorio.*

class Recordatorios : AppCompatActivity() {

    val manejoBDD = ManejoBDD()
    var manejoURL: ManejoURL? = null
    var usuario: Usuario? = null
    //var datosUsuario: Usuario?=null
    var sesion: Sesion? = null
    val manejoDeGUI = ManejoDeGUI()
    var ctx: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recordatorio)

        ctx = this
        //IMPORTANTE: Se inicializa la clase para manejar las solicitudes
        Solicitud.init(this)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this/*  Activity context */)
        var ipServidor = sharedPreferences.getString("ipServidor","").toString()
        manejoURL = ManejoURL(ipServidor)

        var datosUsuario = manejoBDD.leerDatosUsuario(this)
        var nuevosDatos:Usuario?=null
        sesion=manejoBDD.leerSesion(this)

        manejoURL!!.buscarDatosUsuario(this,datosUsuario!!,sesion!!,null)
        datosUsuario = manejoBDD.leerDatosUsuario(this)

        var recibeSMS = datosUsuario?.recibe_sms.toString()
        var recibeEmail = datosUsuario?.recibe_email.toString()

        swtch_recibirSMS?.setOnCheckedChangeListener(null)
        swtch_recibirEmail?.setOnCheckedChangeListener(null)

        if(recibeSMS.equals("si")){
            swtch_recibirSMS.isChecked=true
        }
        else{
            swtch_recibirSMS.isChecked=false
        }

        if(recibeEmail.equals("si")){
            swtch_recibirEmail.isChecked=true
        }
        else{
            swtch_recibirEmail.isChecked=false
        }

        swtch_recibirSMS?.setOnCheckedChangeListener {
                _, activo ->
            if(activo) {
                nuevosDatos= Usuario(
                    ci= datosUsuario?.ci!!,
                    nombre = datosUsuario?.nombre,
                    apellido = datosUsuario?.apellido,
                    direccion = datosUsuario?.direccion,
                    telefono = datosUsuario?.telefono,
                    email = datosUsuario?.email,
                    recibe_email = datosUsuario?.recibe_email,
                    recibe_sms = "si"
                )
                println("Dato leido y pasado a datos: "+ nuevosDatos!!.ci)
            }
            else{
                nuevosDatos= Usuario(
                    ci=datosUsuario?.ci!!,
                    nombre = datosUsuario?.nombre,
                    apellido = datosUsuario?.apellido,
                    direccion = datosUsuario?.direccion,
                    telefono = datosUsuario?.telefono,
                    email = datosUsuario?.email,
                    recibe_email = datosUsuario?.recibe_email,
                    recibe_sms = "no"
                )
            }
            //var mensaje:String="Usuario:"+nuevosDatos!!.ci + " Recibe SMS:" + nuevosDatos!!.recibe_sms
            //Toast.makeText(this, mensaje ,Toast.LENGTH_SHORT).show()
            manejoURL!!.actualizarDatos(this,nuevosDatos!!,sesion!!,10)
        }

        swtch_recibirEmail?.setOnCheckedChangeListener {
                _, activo ->
            if(activo) {
                nuevosDatos= Usuario(
                    ci= datosUsuario?.ci!!,
                    nombre = datosUsuario?.nombre,
                    apellido = datosUsuario?.apellido,
                    direccion = datosUsuario?.direccion,
                    telefono = datosUsuario?.telefono,
                    email = datosUsuario?.email,
                    recibe_email = "si",
                    recibe_sms = datosUsuario?.recibe_sms
                )
            }
            else{
                nuevosDatos= Usuario(
                    ci=datosUsuario?.ci!!,
                    nombre = datosUsuario?.nombre,
                    apellido = datosUsuario?.apellido,
                    direccion = datosUsuario?.direccion,
                    telefono = datosUsuario?.telefono,
                    email = datosUsuario?.email,
                    recibe_email = "no",
                    recibe_sms = datosUsuario?.recibe_sms
                )
            }
            //var mensaje:String="Usuario:"+nuevosDatos!!.ci + " Recibe SMS:" + nuevosDatos!!.recibe_sms
            //Toast.makeText(this, mensaje ,Toast.LENGTH_SHORT).show()
            manejoURL!!.actualizarDatos(this,nuevosDatos!!,sesion!!,10)
        }


    }



    fun volverAMenu(v: View){
        val intent = Intent(v.context, MenuPrincipal::class.java)
        startActivity(intent)
    }

}
