package com.example.systurnomobile.BDD

import android.content.Context
import android.os.Environment
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
    Sesion::class,
    Usuario::class,
    MedicamentoRecetado::class,
    Turno::class,
    MisTurnos::class,
    MisRecetas::class
    ],
    version = 1)
abstract class BaseDeDatos:RoomDatabase() {

    //Provee acceso a los métodos relacionados a las consultas
    abstract fun sesionDao():SesionDAO
    abstract fun usuarioDao():UsuarioDAO
    abstract fun medicamentoRecetadoDao():MedicamentoRecetadoDAO
    abstract fun turnoDao():TurnoDAO
    abstract fun misTurnosDao():MisTurnosDAO
    abstract fun misRecetasDao():MisRecetasDAO

    //Permite acceso mediante un "singleton", que permite usar los métodos sin generar instancias
    companion object{
        var INSTANCIA:BaseDeDatos? = null

        fun obtenerBDD(context: Context): BaseDeDatos?{
            if(INSTANCIA == null){
                synchronized(BaseDeDatos::class){
                    INSTANCIA = Room.databaseBuilder(
                        context.applicationContext,
                        BaseDeDatos::class.java,
                        "systurno"
                        //Environment.getExternalStorageDirectory().absolutePath+"/systurno"
                    ).build()
                }
            }
            return INSTANCIA
        }

        fun cerrarBDD(){
            INSTANCIA = null
        }
    }
}