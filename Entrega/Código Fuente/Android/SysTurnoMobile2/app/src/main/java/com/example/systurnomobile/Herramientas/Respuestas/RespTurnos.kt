package com.example.systurnomobile.Herramientas.Respuestas

import com.example.systurnomobile.BDD.Turno
import org.json.JSONArray

class RespTurnos(respuesta:String):Respuesta(respuesta) {

    /**
     * Devuelve la lista de turnos recibida del servidor
     */
    fun listaTurnos():List<Turno>{
        var lista:MutableList<Turno> = ArrayList<Turno>()

        var datosServidor = analizarRespuesta("Turnos","turnos","ERROR de lectura en el JSON")

        var listado=JSONArray(datosServidor)

        for (i in 0 until listado.length()){
            var item = listado.getJSONObject(i)
            var turno = Turno(
                id = item.getInt("id"),
                estado = item.getString("estado"),
                fechahora = item.getString("fechaHora")
            )
            lista.add(turno)
        }
        return lista
    }
}