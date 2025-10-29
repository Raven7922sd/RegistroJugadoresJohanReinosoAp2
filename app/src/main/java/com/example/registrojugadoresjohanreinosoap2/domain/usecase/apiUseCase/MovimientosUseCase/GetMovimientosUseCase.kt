package com.example.registrojugadoresjohanreinosoap2.domain.usecase.apiUseCase.JugadoresApiUseCase


import com.example.registrojugadoresjohanreinosoap2.domain.model.Movimiento.Movimientos
import com.example.registrojugadoresjohanreinosoap2.domain.repository.ApiRepository.MovimientoApiRepository
import javax.inject.Inject

class GetMovimientosUseCase @Inject constructor(
    private val repository: MovimientoApiRepository
) {
    suspend operator fun invoke(partidaId: Int): List<Movimientos> {
        return repository.getMovimientos(partidaId)
    }
}