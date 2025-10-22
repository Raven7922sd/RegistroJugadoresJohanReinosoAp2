package edu.ucne.registrojugadores.Domain.UseCase.ApiUseCase.MovimientosUseCase

import com.example.registrojugadoresjohanreinosoap2.domain.model.Movimiento.Movimientos
import com.example.registrojugadoresjohanreinosoap2.domain.repository.ApiRepository.MovimientoApiRepository
import javax.inject.Inject

class PostMovimientosUseCase @Inject constructor(
    private val repository: MovimientoApiRepository
) {
    suspend operator fun invoke(movimiento: Movimientos): Boolean {
        return repository.crearMovimiento(movimiento)
    }
}