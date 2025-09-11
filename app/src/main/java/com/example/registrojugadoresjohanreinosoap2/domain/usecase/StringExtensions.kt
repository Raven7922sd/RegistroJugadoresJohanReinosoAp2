package com.example.registrojugadoresjohanreinosoap2.domain.usecase


fun String.normalize(): String {
    val temp = java.text.Normalizer.normalize(this, java.text.Normalizer.Form.NFD)
    return Regex("\\p{InCombiningDiacriticalMarks}+").replace(temp, "").lowercase()
}