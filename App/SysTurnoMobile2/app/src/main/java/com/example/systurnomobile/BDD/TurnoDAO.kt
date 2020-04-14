package com.example.systurnomobile.BDD

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TurnoDAO {
    @Insert
    fun nuevoTurno(turno:Turno)

    @Query("SELECT * FROM Turno ORDER BY id ASC LIMIT 1")
    fun primerTurno():List<Turno>

    @Query("SELECT * FROM Turno WHERE id = :id")
    fun buscarTurnoPorID(id:Int):Turno

    @Query("SELECT * FROM Turno WHERE id > :id ORDER BY id ASC LIMIT 1")
    fun buscarTurnoSig(id:Int):List<Turno>

    @Query("SELECT * FROM Turno WHERE id < :id ORDER BY id DESC LIMIT 1")
    fun buscarTurnoAnt(id:Int):List<Turno>

    @Query("DELETE FROM Turno")
    fun borrarTurnos()
}