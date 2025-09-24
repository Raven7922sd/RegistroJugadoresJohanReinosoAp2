package com.example.registrojugadoresjohanreinosoap2.data.local.Logros

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Logros")
class LogroEntity (
@PrimaryKey(autoGenerate = true )
val LogroId: Int=0,
val LogroNombre: String = "",
val Descripcion: String = "",
val EsDesbloqueado: Boolean = false
)
{

}