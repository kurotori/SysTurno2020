package com.example.systurnomobile.BDD

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Usuario(
    @PrimaryKey (autoGenerate = false) val ci: Int,
    @ColumnInfo (name = "nombre") val nombre:String? = null,
    @ColumnInfo (name = "apellido") val apellido: String? = null,
    @ColumnInfo (name="telefono") val telefono: String?= null,
    @ColumnInfo (name = "direccion") val direccion: String?= null,
    @ColumnInfo (name = "email") val email: String?= null,
    @ColumnInfo (name = "recibe_sms") val recibe_sms: String?= null,
    @ColumnInfo (name = "recibe_email") val recibe_email: String?= null
)

/*
    nombre varchar(30) NOT NULL,
	apellido varchar(30) NOT NULL,
	telefono varchar(12) NOT NULL,
	direccion varchar(40) NOT NULL,
	email varchar(45) NOT NULL,
	recibeSMS enum('si','no') NOT NULL default 'si',
	recibeEmail enum('si','no') NOT NULL default 'si'
 */