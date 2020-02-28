package com.example.pruebas03.ManejoAPI

import android.content.Context
import com.android.volley.*
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.StringRequest
import com.example.pruebas03.Modelos.DatosPrueba

class ConexionAPI(private val ctx: Context) {

    private fun ejecutarSolicitud(conexionServer: ConexionServer,
                                  finalizacion: (exito: Boolean, respuestaApi: Respuesta) -> Unit){
        val solicitud: StringRequest =
            object: StringRequest(conexionServer.httpMethod,
                                  conexionServer.url,
                                  {respuesta -> this.manejar(respuesta, finalizacion)},
                                  {it.printStackTrace()
                                       if (it.networkResponse != null && it.networkResponse.data != null)
                                           this.manejar(String(it.networkResponse.data), finalizacion)
                                       else
                                           this.manejar(errorAString(it), finalizacion)
                                  })
            {
                override fun getParams(): MutableMap<String, String> {
                    return conexionServer.params
                }

                override fun getHeaders(): MutableMap<String, String> {
                    return conexionServer.headers
                }
            }
        solicitud.retryPolicy = DefaultRetryPolicy(
                                    conexionServer.espera,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                )

        obtenerFilaSolicitudes().add(solicitud)
    }

    private fun manejar(respuesta: String,
                        finalizacion: (exito: Boolean, respuestaApi: Respuesta) -> Unit) {
        val ar = Respuesta(respuesta)
        finalizacion.invoke(ar.exito, ar)
    }

    private fun errorAString(volleyError: VolleyError): String {
        return when (volleyError) {
            is TimeoutError -> "La conexión tomo demasiado tiempo."
            is NoConnectionError -> "No se pudo establecer una conexión."
            is AuthFailureError -> "Error de autenticación."
            is ServerError -> "Hubo un error procesando la respuesta del servidor."
            is NetworkError -> "Error de conexión."
            is ParseError -> "Hubo un error procesando la respuesta del servidor."
            else -> "Error de conexión"
        }
    }

    private fun obtenerFilaSolicitudes(): RequestQueue {
        val maxCacheSize = 20 * 1024 * 1024
        val cache = DiskBasedCache(ctx.cacheDir, maxCacheSize)
        val netWork = BasicNetwork(HurlStack())
        val mRequestQueue = RequestQueue(cache, netWork)
        mRequestQueue.start()
        System.setProperty("http.keepAlive", "false")
        return mRequestQueue
    }

    fun verDatosPrueba(finalizacion: (datos: List<DatosPrueba>?, mensaje:String) -> Unit){
        val servidor = ConexionServer.DatoPrueba(ctx)
        ejecutarSolicitud(servidor){exito, respuestaApi ->
            if(exito){
                val datos: List<DatosPrueba> = respuestaApi.json.toArray()
                finalizacion.invoke(datos,"ok")
            }
            else{
                finalizacion.invoke(null,respuestaApi.mensaje)
            }
        }


    }



    /*fun getAllCountries(completion: (countries: List<Country>?, message: String) -> Unit) {
        val route = ApiRoute.GetAll(ctx)
        performRequest(route) { success, response ->
            if (success) {
                val countries: List<Country> =  response.json.toArray()
                completion.invoke(countries, "")
            } else {
                completion.invoke(null, response.message)
            }
        }
    }*/
}