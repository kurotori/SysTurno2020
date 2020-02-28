package com.example.pruebas03.API2

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class Solicitud(val url:String,
                val resultado:(JSONObject)->Unit,
                val error: (String?)->Unit
                ) {

    fun POST(vararg params: Pair<String, Any>) {
        // HashMap to pass arguments to Volley
        val hashMap = HashMap<String, String>()
        params.forEach {
            // Convert all Any type to String and add to HashMap
            hashMap[it.first] = it.second.toString()
        }
        // Make the Http Request
        makeRequest(Request.Method.POST, hashMap)
    }

    private fun makeRequest(method: Int, params: HashMap<String, String>) {
        // Creating a StringRequest
        val req = object : StringRequest(method, url, { res ->

            // Creating JSON object from the response string
            // and passing it to result: (JSONObject) -> Unit function
            resultado(JSONObject(res.toString().trim()))
        }, { volleyError ->

            // Getting error message and passing it
            // to val error: (String) -> Unit function
            error(volleyError.message)
        }) {
            // Overriding getParams() to pass our parameters
            override fun getParams(): MutableMap<String, String> {
                return params
            }
        }

        // Adding request to the queue
        volley.add(req)
    }

    companion object {
        var context: Context? = null
        val volley: RequestQueue by lazy {
            Volley.newRequestQueue(context
                ?: throw NullPointerException(" Initialize WolfRequest in application class"))
        }

        fun init(context: Context) {
            this.context = context
        }
    }
}

