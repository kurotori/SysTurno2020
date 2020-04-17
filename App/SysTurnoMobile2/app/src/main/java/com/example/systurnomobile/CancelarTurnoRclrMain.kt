package com.example.systurnomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.systurnomobile.Fragmentos.CancelarTurnoRclrFragment
import com.example.systurnomobile.Fragmentos.SingleFragmentActivity

class CancelarTurnoRclrMain : SingleFragmentActivity() {

    override fun createFragment() = CancelarTurnoRclrFragment.newInstance()

    fun volverAMenu(v: View){
        val intent = Intent(v.context, MenuPrincipal::class.java)
        startActivity(intent)
    }



}
