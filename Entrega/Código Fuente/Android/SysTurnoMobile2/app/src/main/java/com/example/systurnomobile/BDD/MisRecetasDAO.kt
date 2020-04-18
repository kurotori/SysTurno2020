package com.example.systurnomobile.BDD

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MisRecetasDAO {
    @Insert
    fun nuevaReceta(receta:MisRecetas)

    @Query("DELETE FROM MisRecetas")
    fun borrarTodasMisRecetas()

    @Query("SELECT * FROM MisRecetas")
    fun todasMisRecetas():List<MisRecetas>

    @Query("SELECT * FROM MisRecetas WHERE turno_id = :turno_id")
    fun buscarRecetasDelTurno(turno_id:Int):List<MisRecetas>

}