package com.example.systurnomobile.Fragmentos

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.systurnomobile.R

class DialogoEspera: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //return super.onCreateDialog(savedInstanceState)
        return activity?.let {
            val constr = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            constr.setView(inflater.inflate(R.layout.dialogo_espera,null))
            constr.setMessage("Procesando su solicitud.")
            constr.create()
        }?: throw IllegalStateException("Activity cannot be null")
    }


}