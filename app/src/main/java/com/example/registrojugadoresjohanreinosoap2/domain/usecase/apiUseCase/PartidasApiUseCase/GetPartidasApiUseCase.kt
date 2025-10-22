package edu.ucne.registrojugadores.Domain.UseCase.ApiUseCase.PartidasApiUseCase

import com.example.registrojugadoresjohanreinosoap2.domain.model.Game
import com.example.registrojugadoresjohanreinosoap2.domain.repository.ApiRepository.PartidaApiRepository
import javax.inject.Inject

class GetPartidasApiUseCase @Inject constructor(
    private val repository: PartidaApiRepository
) {
    suspend operator fun invoke(id: Int): Game? {
        return repository.getPartida(id)
    }
}