package com.example.systurnomobile.BDD

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

    @Entity
    data class Sesion(
        @PrimaryKey (autoGenerate = true) val id: Int? = null,
        @ColumnInfo (name = "token") val token: String?,
        @ColumnInfo (name = "idSesion") val idSesion: String?,
        @ColumnInfo (name = "mensaje") val mensaje:String?
    )
