package com.example.systurno_pruebas01

import android.content.Context
import com.android.volley.*
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ApiCliente(private val ctx: Context) {
    /***
     * PERFORM REQUEST
     */
    private fun performRequest(route: ApiRuta,
                               completion: (success: Boolean,
                                            apiResponse: ApiRespuesta
                                            ) -> Unit)
    {
        val request: StringRequest = object : StringRequest(route.httpMethod, route.url, { response ->
            this.handle(response, completion)
        }, {
            it.printStackTrace()
            if (it.networkResponse != null && it.networkResponse.data != null)
                this.handle(String(it.networkResponse.data), completion)
            else
                this.handle(getStringError(it), completion)
        }) {
            override fun getParams(): MutableMap<String, String> {
                return route.params
            }

            override fun getHeaders(): MutableMap<String, String> {
                return route.headers
            }
        }
        request.retryPolicy =
            DefaultRetryPolicy(
                route.timeOut,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        getRequestQueue().add(request)
    }

    /**
     * This method will make the creation of the answer as ApiResponse
     **/
    private fun handle(response: String,
                       completion: (success: Boolean,
                                    apiResponse: ApiRespuesta) -> Unit) {
        val ar = ApiRespuesta(response)
        completion.invoke(ar.exito, ar)
    }

    /**
     * This method will return the error as String
     **/
    private fun getStringError(volleyError: VolleyError): String {
        return when (volleyError) {
            is TimeoutError -> "The conection timed out."
            is NoConnectionError -> "The conection couldn´t be established."
            is AuthFailureError -> "There was an authentication failure in your request."
            is ServerError -> "Error while prosessing the server response."
            is NetworkError -> "Network error, please verify your conection."
            is ParseError -> "Error while prosessing the server response."
            else -> "Internet error"
        }
    }
    /**
     * We create and return a new instance for the queue of Volley requests.
     **/
    private fun getRequestQueue(): RequestQueue {
        val maxCacheSize = 20 * 1024 * 1024
        val cache = DiskBasedCache(ctx.cacheDir, maxCacheSize)
        val netWork = BasicNetwork(HurlStack())
        val mRequestQueue = RequestQueue(cache, netWork)
        mRequestQueue.start()
        System.setProperty("http.keepAlive", "false")
        return mRequestQueue
    }

    //----------------------------------------------

    fun loginUser(email: String, password: String, completion: (user: User?, message: String) -> Unit) {
        val route = ApiRuta.Login(email, password, ctx)
        this.performRequest(route) { success, response ->
            if (success) {
                // this object creation could be created at the other class
                // like *ApiResponseManager* and do a CRUD if it is necesary
                val user: User = response.json.toObject()
                completion.invoke(user, "")
            } else {
                completion.invoke(null, response.mensaje)
            }
        }
    }

   fun verDatosServidor(completion: (dato:DatoServidor?, message: String) -> Unit){
        val ruta = ApiRuta.LeerJsonServidor(ctx)
        this.performRequest(ruta){success, response ->
            if (success){
                val dato: DatoServidor = response.json.toObject()
                completion.invoke(dato,"")
            }
            else{
                completion.invoke(null, response.mensaje)
            }

        }
    }
    //-------------------------------------------------------------------



}