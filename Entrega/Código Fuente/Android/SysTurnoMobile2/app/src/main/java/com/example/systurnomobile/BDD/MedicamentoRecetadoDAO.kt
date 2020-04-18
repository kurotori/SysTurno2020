package com.example.systurnomobile.BDD

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
    interface MedicamentoRecetadoDAO{
        @Insert
        fun nuevoMedicamentoRecetado(medicamento:MedicamentoRecetado)

        @Query("SELECT * FROM MedicamentoRecetado")
        fun todosLosMedicamentosRecetados():List<MedicamentoRecetado>

        @Query("DELETE FROM MedicamentoRecetado")
        fun borrarTodosLosMedicamentos()
    }