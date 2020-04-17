package com.example.systurnomobile.Herramientas.Respuestas

import com.example.systurnomobile.BDD.MisTurnos
import com.example.systurnomobile.BDD.Turno
import org.json.JSONArray

class RespMisTurnos(respuesta:String):Respuesta(respuesta) {
    /**
     * Devuelve la lista de turnos recibida del servidor
     */
    fun listaMisTurnos():List<MisTurnos>{
        var lista:MutableList<MisTurnos> = ArrayList<MisTurnos>()

        var datosServidor = analizarRespuesta("Turnos","turnos","ERROR de lectura en el JSON")

        var listado= JSONArray(datosServidor)

        for (i in 0 until listado.length()){
            var item = listado.getJSONObject(i)
            var mt = MisTurnos(
                id = item.getInt("id"),
                estado = item.getString("estado"),
                fechahora = item.getString("fechaHora")
            )
            lista.add(mt)
        }
        return lista
    }
}