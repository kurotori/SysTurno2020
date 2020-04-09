package com.example.systurnomobile.Fragmentos

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.systurnomobile.R

/**
 * Esta clase servirá de base a los ReciclerView necesarios
 */
abstract class SingleFragmentActivity : AppCompatActivity() {

    private val layoutResId: Int
        @LayoutRes
        get() = R.layout.fragmento_actividades

    protected abstract fun createFragment(): Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(layoutResId)

        val fm = supportFragmentManager
        //contenedor_de_fragmentos es un FrameLayout en el layout fragmento_actividades.xml
        var fragment = fm.findFragmentById(R.id.contenedor_de_fragmentos)

        // ensures fragments already created will not be created
        if (fragment == null) {
            fragment = createFragment()
            // create and commit a fragment transaction
            fm.beginTransaction()
                .add(R.id.contenedor_de_fragmentos, fragment)
                .commit()
        }
    }
}