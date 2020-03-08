package com.example.systurnomobile.BDD

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

    @Dao
    interface SesionDAO{
        @Query("SELECT * FROM Sesion ORDER BY id LIMIT 1")
        fun ultimaSesion():List<Sesion>

        @Insert
        fun nuevaSesion(vararg sesion: Sesion)
    }