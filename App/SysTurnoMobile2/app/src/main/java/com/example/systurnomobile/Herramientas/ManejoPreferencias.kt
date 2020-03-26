package com.example.systurnomobile.Herramientas

import android.content.Context
import androidx.preference.PreferenceManager

class ManejoPreferencias(ctx:Context) {
    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx/*  Activity context */)

    public fun obtenerServidor():String {
        var ipServidor:String = ""
        ipServidor = sharedPreferences.getString("ipServidor","").toString()
        return ipServidor
    }

}