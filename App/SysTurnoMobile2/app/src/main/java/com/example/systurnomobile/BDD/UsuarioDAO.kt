package com.example.systurnomobile.BDD

import androidx.room.*


@Dao
interface UsuarioDAO {
    @Query("SELECT * FROM Usuario ORDER BY ci DESC LIMIT 1")
    fun verDatosUsuario():List<Usuario>

    @Insert
    fun nuevoUsuario(usuario:Usuario)

    @Delete
    fun borrarUsuario(usuario: Usuario)

    @Query("DELETE FROM Usuario")
    fun borrarTodosLosUsuarios()

    @Update
    fun actualizarDatosUsuario(usuario: Usuario)
}