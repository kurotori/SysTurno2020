package com.example.systurnomobile

import android.content.Intent
import android.view.View
import com.example.systurnomobile.Fragmentos.SingleFragmentActivity
import com.example.systurnomobile.Fragmentos.SolicitarTurnoRclrFragment

class SolicitarTurnoRclrMain:SingleFragmentActivity() {
    override fun createFragment() = SolicitarTurnoRclrFragment.newInstance()

    fun volverAMenu(v: View){
        val intent = Intent(v.context, MenuPrincipal::class.java)
        startActivity(intent)
    }

}