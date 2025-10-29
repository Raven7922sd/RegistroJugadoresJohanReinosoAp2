package com.example.registrojugadoresjohanreinosoap2.data.mapper.LogroMappers

import com.example.registrojugadoresjohanreinosoap2.data.local.Logros.LogroEntity
import com.example.registrojugadoresjohanreinosoap2.domain.model.Logros.Logros

fun LogroEntity.toLogro(): Logros {
    return Logros(
        LogroId = LogroId,
        LogroNombre = LogroNombre,
        Descripcion = Descripcion,
        EsDesbloqueado = EsDesbloqueado
    )
}

fun Logros.toEntity(): LogroEntity {
    return LogroEntity(
        LogroId = LogroId,
        LogroNombre = LogroNombre,
        Descripcion = Descripcion,
        EsDesbloqueado = EsDesbloqueado
    )
}