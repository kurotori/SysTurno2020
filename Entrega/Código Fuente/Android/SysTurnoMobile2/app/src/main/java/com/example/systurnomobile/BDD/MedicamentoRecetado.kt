package com.example.systurnomobile.BDD

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MedicamentoRecetado (
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "receta_id") val receta_id: Int,
    @ColumnInfo(name = "fecha") val fecha:String,
    @ColumnInfo(name = "especialista") val especialista:String,
    @ColumnInfo(name = "med_id") val med_id:Int,
    @ColumnInfo(name = "med_nombre") val med_nombre:String,
    @ColumnInfo(name = "cantidad") val cantidad:Int,
    @ColumnInfo(name = "disponibilidad") val disponibilidad:String,
    @ColumnInfo(name = "entregado") val entregado:String
)

/*
val receta_id:Int,
    val fecha: String,
    val especialista: String,
    val med_id: Int,
    val med_nombre: String,
    val cantidad: Int,
    val disponibilidad: String,
    val entregado: String
 */