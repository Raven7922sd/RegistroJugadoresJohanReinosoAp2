package com.example.registrojugadoresjohanreinosoap2.domain.usecase.playerUseCase

import java.text.Normalizer


fun String.normalize(): String {
    val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
    return Regex("\\p{InCombiningDiacriticalMarks}+").replace(temp, "").lowercase()
}