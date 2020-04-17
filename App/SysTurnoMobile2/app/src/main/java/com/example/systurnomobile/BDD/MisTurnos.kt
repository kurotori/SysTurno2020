package com.example.systurnomobile.BDD

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class MisTurnos (
    @PrimaryKey(autoGenerate = false) val id: Int,
    @ColumnInfo(name = "fechahora") val fechahora: String,
    @ColumnInfo(name = "estado") val estado:String,
    @ColumnInfo(name = "recetas") val recetas:String?=null
)
