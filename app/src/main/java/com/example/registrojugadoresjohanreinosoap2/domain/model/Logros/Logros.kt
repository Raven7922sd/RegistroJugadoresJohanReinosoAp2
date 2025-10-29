package com.example.registrojugadoresjohanreinosoap2.domain.model.Logros

import androidx.room.PrimaryKey

class Logros (
    @PrimaryKey (autoGenerate = true)
    val LogroId: Int =0,
    val LogroNombre: String ="",
    val Descripcion: String ="",
    val EsDesbloqueado: Boolean =false
) {
}