package com.example.systurnomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceManager
import com.example.systurnomobile.BDD.Usuario
import com.example.systurnomobile.Herramientas.ManejoDeGUI
import com.example.systurnomobile.Herramientas.ManejoURL
import com.example.systurnomobile.Herramientas.Solicitud
import kotlinx.android.synthetic.main.registro_nuevo.*

class Registro : AppCompatActivity() {
    var manejoURL: ManejoURL? = null
    var manejoDeGUI = ManejoDeGUI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_nuevo)

        //IMPORTANTE: Se inicializa la clase para manejar las solicitudes
        Solicitud.init(this)

        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this)
        var ipServidor = sharedPreferences.getString("ipServidor","").toString()
        manejoURL = ManejoURL(ipServidor)
    }

    fun registrarUsuario(v: View){
        val ctx = v.context
        var usuario_ci = et_registroCI.text.toString()
        var nombre = et_registroNombre.text.toString()
        var apellido = et_registroApellidos.text.toString()
        var direccion = et_registroDireccion.text.toString()
        var telefono = etRegistroTelefono.text.toString()
        var email = et_registroEmail.text.toString()
        var contrasenia = et_registroContrasenia.text.toString()
        var repContrasenia = et_registroRepContrasenia.text.toString()

        if (usuario_ci.length==0) {
            manejoDeGUI.mostrarAdvertencia("Error","Debe ingresar su Cédula de Identidad",ctx)?.show()
        }
        else{
            if (contrasenia.equals(repContrasenia)) {
                var usuario = Usuario(
                    ci = usuario_ci.toInt(),
                    nombre = nombre,
                    apellido = apellido,
                    direccion = direccion,
                    telefono = telefono,
                    email = email
                )
                //manejoDeGUI.mostrarDialogoEspera()
                manejoURL?.registrarUsuario(ctx, usuario, contrasenia)

            } else {
                manejoDeGUI.mostrarAdvertencia("Error", "Las contraseñas no coinciden", ctx)?.show()
            }
        }


    }

    fun volverAlInicio(v: View){
        var intent = Intent(v.context,PantallaInicial::class.java)
        startActivity(intent)
    }

}
