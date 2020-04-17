package com.example.systurnomobile.BDD

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MisTurnosDAO{
    @Insert
    fun nuevoTurno(turno:MisTurnos)

    @Query("SELECT * FROM MisTurnos ORDER BY id ASC")
    fun todosMisTurnos():List<MisTurnos>

    @Query("SELECT * FROM MisTurnos WHERE estado = 'confirmado'")
    fun miTurnoSolicitado():List<MisTurnos>

    @Query("DELETE FROM MisTurnos")
    fun borrarMisTurnos()
}