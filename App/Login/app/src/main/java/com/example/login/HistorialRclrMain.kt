package com.example.login

import androidx.fragment.app.FragmentActivity


/**
 * Esta clase servira como base para una SingleFragmentActivity
 * Ver Referencias:
 * SingleFragmentActivity --> https://medium.com/@hinchman_amanda/the-singlefragmentactivity-pattern-in-android-kotlin-ce93385252e5
 * ReciclerView --> https://medium.com/@hinchman_amanda/working-with-recyclerview-in-android-kotlin-84a62aef94ec
 */
class HistorialRclrMain: SingleFragmentActivity()  {
    override fun createFragment() = HistorialRclrFragment.newInstance()
}