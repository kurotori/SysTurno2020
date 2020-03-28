package com.example.systurnomobile.BDD

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface UsuarioDAO {
    @Query("SELECT * FROM Usuario ORDER BY ciUsuario DESC LIMIT 1")
    fun verDatosUsuario():List<Usuario>

    @Insert
    fun nuevoUsuario(usuario:Usuario)
}