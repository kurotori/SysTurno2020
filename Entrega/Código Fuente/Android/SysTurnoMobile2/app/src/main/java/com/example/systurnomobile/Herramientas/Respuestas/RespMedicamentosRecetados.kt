package com.example.systurnomobile.Herramientas.Respuestas
//kotlin.collections CollectionsKt.class
import com.example.systurnomobile.BDD.MedicamentoRecetado
import org.json.JSONArray
import kotlin.collections.Collection

class RespMedicamentosRecetados(respuesta:String):Respuesta(respuesta) {


    fun listaMeds():List<MedicamentoRecetado>?{
        var lista:MutableList<MedicamentoRecetado>?=ArrayList<MedicamentoRecetado>()

        var prueba = analizarRespuesta("ListadoMedRecetados","medicamentos","ERROR de lectura en el JSON")
        println(prueba)

        var listado = JSONArray(prueba)

        println("Leyendo el array de medicinas:"+listado.length())
        for (i in 0 until listado.length()){
            var item = listado.getJSONObject(i)
            var med = MedicamentoRecetado(
                receta_id = item.getInt("receta_id"),
                fecha = item.getString("fecha"),
                especialista = item.getString("especialista"),
                med_id = item.getInt("med_id"),
                med_nombre = item.getString("med_nombre"),
                cantidad= item.getInt("cantidad"),
                disponibilidad = item.getString("disponibilidad"),
                entregado = item.getString("entregado")
            )
            println("de lista JSON:"+med.med_nombre)
            lista?.add(med)
        }
        println("Longitud de lista procesada desde la respuesta"+lista?.size)
        return lista?.toList()
    }
}