package com.example.systurnomobile.BDD

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

    @Entity
    data class Sesion(
        @PrimaryKey (autoGenerate = true) val id: Int? = null,
        @ColumnInfo (name = "tokenVal") val tokenVal: String?,
        @ColumnInfo (name = "tokenId") val tokenId: String?,
        @ColumnInfo (name = "sesionVal") val sesionVal: String?,
        @ColumnInfo (name = "sesionId") val sesionId: String?,
        @ColumnInfo (name = "mensaje") val mensaje:String?
    )
