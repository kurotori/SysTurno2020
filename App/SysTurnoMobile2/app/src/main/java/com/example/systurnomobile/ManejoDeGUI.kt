package com.example.systurnomobile

import android.content.Intent
import android.view.View
import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity

class ManejoDeGUI {

    /**
     * Permite cambiar de Layout
     */
    fun cambiarLayout(){

    }

    fun irAMenuPrincipal(v: View){
        val ctx = v.context
        val intent: Intent = Intent(ctx,MenuPrincipal::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("EXIT", true)
        ContextCompat.startActivity(ctx,intent,null)
    }

    fun irAMenuPrincipal(ctx: Context){
        val intent: Intent = Intent(ctx,MenuPrincipal::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("EXIT", true)
        ContextCompat.startActivity(ctx,intent,null)
    }

    fun irAIniciarSesion(v:View){
        val ctx = v.context
        val intent: Intent = Intent(ctx,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("EXIT", true)
        ContextCompat.startActivity(ctx,intent,null)
    }

    fun irAIniciarSesion(ctx: Context){

        val intent: Intent = Intent(ctx,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("EXIT", true)
        ContextCompat.startActivity(ctx,intent,null)
    }

}