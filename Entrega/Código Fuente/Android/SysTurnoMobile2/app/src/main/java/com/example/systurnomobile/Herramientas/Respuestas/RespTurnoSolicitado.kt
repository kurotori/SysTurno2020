package com.example.systurnomobile.Herramientas.Respuestas

import com.example.systurnomobile.BDD.MedicamentoRecetado
import com.example.systurnomobile.BDD.MisRecetas
import com.example.systurnomobile.BDD.MisTurnos
import org.json.JSONArray

class RespTurnoSolicitado(respuesta:String):Respuesta(respuesta) {

    /**
     * Devuelve el turno que el usuario ha solicitado en el sistema
     */
    fun turnoSolicitado():MisTurnos{
        return MisTurnos(
            id = analizarRespuesta("TurnoSolicitado","id","0").toInt(),
            estado = analizarRespuesta("TurnoSolicitado","estado","no_estado"),
            fechahora = analizarRespuesta("TurnoSolicitado","fechaHora","no_fechaHora")
        )
    }

    fun recetasDelTurno():List<MisRecetas>{
        var turno = turnoSolicitado()
        var lista:MutableList<MisRecetas> = ArrayList()
        var datosServidor = analizarRespuesta("TurnoSolicitado","recetasDelTurno","ERROR de lectura en el JSON")

        var listado= JSONArray(datosServidor)

        for (i in 0 until listado.length()) {
            var item = listado.getJSONObject(i)
            var mr:MisRecetas = MisRecetas(
                id = item.getInt("id"),
                fecha = item.getString("fecha"),
                especialista = item.getString("especialista"),
                turno_id = turno.id,
                medicamentos = item.getString("medicamentos")
            )
            lista.add(mr)
        }

        return lista
    }

    fun medicamentosDelTurno():List<MedicamentoRecetado>{
        var recetas = recetasDelTurno()
        var lista:MutableList<MedicamentoRecetado> = ArrayList()

        recetas.forEach {
            val meds = it.medicamentos
            val listado = JSONArray(meds)
            for (i in 0 until listado.length()) {
                var item = listado.getJSONObject(i)
                var med = MedicamentoRecetado(
                    med_id = item.getInt("med_id"),
                    med_nombre = item.getString("med_nombre"),
                    cantidad = item.getInt("cantidad"),
                    disponibilidad = item.getString("disponibilidad"),
                    entregado = item.getString("entregado"),
                    especialista = it.especialista,
                    fecha = it.fecha,
                    receta_id = it.id
                )
                lista?.add(med)
            }
        }
        return lista
    }

}