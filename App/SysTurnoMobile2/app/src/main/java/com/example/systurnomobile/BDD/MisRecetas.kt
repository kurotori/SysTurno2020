package com.example.systurnomobile.BDD

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class MisRecetas (
    @PrimaryKey(autoGenerate = false) val id: Int,
    @ColumnInfo(name = "fecha") val fecha: String,
    @ColumnInfo(name = "especialista") val especialista: String,
    @ColumnInfo(name = "turno_id") val turno_id: Int,
    @ColumnInfo(name = "medicamentos") val medicamentos:String? = null
)