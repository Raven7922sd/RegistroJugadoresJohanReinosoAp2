package com.example.registrojugadoresjohanreinosoap2.presentation.apiPresentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registrojugadoresjohanreinosoap2.domain.model.Game
import com.example.registrojugadoresjohanreinosoap2.domain.model.Movimiento.Movimientos
import com.example.registrojugadoresjohanreinosoap2.domain.model.Player
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.apiUseCase.JugadoresApiUseCase.GetJugadoresUseCase
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.apiUseCase.JugadoresApiUseCase.GetMovimientosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrojugadores.Domain.UseCase.ApiUseCase.MovimientosUseCase.PostMovimientosUseCase
import edu.ucne.registrojugadores.Domain.UseCase.ApiUseCase.PartidasApiUseCase.GetPartidasApiUseCase
import edu.ucne.registrojugadores.Domain.UseCase.ApiUseCase.PartidasApiUseCase.PostPartidasApiUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class GameApiViewModel @Inject constructor(
    private val getPartidaUseCase: GetPartidasApiUseCase,
    private val postPartidaUseCase: PostPartidasApiUseCase,
    private val getMovimientosUseCase: GetMovimientosUseCase,
    private val postMovimientoUseCase: PostMovimientosUseCase,
    private val getJugadoresUseCase: GetJugadoresUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ApiGameUiState())
    val state: StateFlow<ApiGameUiState> = _state.asStateFlow()
    private var partidaId: Int? = null

    init {
        cargarJugadores()
    }

    private fun cargarJugadores() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, message = "Cargando jugadores") }
            try {
                val jugadores = getJugadoresUseCase()
                _state.update {
                    it.copy(
                        jugadores = jugadores,
                        isLoading = false,
                        message = "${jugadores.size} jugadores cargados"
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        message = "Error al cargar jugadores: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun cargarPartida(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, message = "Cargando partida $id...") }
            try {
                val partida = getPartidaUseCase(id)
                if (partida != null) {
                    partidaId = partida.Gameid

                    val movimientos = getMovimientosUseCase(id)

                    val tablero = reconstruirTableroDesdeMovimientos(movimientos)
                    val jugadorActual = determinarJugadorActual(movimientos)
                    val ganador = checkWinner(tablero)
                    val esEmpate = movimientos.size == 9 && ganador == null

                    val jugador1 = state.value.jugadores.find { it.Jugadorid == partida.JugadorId1 }
                    val jugador2 = state.value.jugadores.find { it.Jugadorid == partida.JugadorId2 }

                    _state.update { currentState ->
                        currentState.copy(
                            board = tablero,
                            movimientos = movimientos,
                            currentPlayer = jugadorActual,
                            winner = ganador,
                            isDraw = esEmpate,
                            gameStarted = true,
                            isLoading = false,
                            jugador1Seleccionado = jugador1,
                            jugador2Seleccionado = jugador2,
                            message = "Partida $partidaId cargada - ${movimientos.size} movimientos"
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            message = "Partida $id no encontrada",
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        message = "Error al cargar partida: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun cargarPartida() {
        val idText = _state.value.partidaIdInput
        if (idText.isBlank()) {
            _state.update { it.copy(message = "Ingresa un ID de partida") }
            return
        }

        val id = idText.toIntOrNull()
        if (id == null) {
            _state.update { it.copy(message = "ID debe ser un número") }
            return
        }

        cargarPartida(id)
    }

    fun updatePartidaId(id: String) {
        _state.update { it.copy(partidaIdInput = id) }
    }

    fun selectJugador1(jugador: Player?) {
        _state.update { it.copy(jugador1Seleccionado = jugador) }
    }

    fun selectJugador2(jugador: Player?) {
        _state.update { it.copy(jugador2Seleccionado = jugador) }
    }

    fun startGame() {
        val jugador1 = _state.value.jugador1Seleccionado
        val jugador2 = _state.value.jugador2Seleccionado

        if (jugador1 == null || jugador2 == null) {
            _state.update { it.copy(message = "Selecciona ambos jugadores") }
            return
        }

        if (jugador1.Jugadorid == jugador2.Jugadorid) {
            _state.update { it.copy(message = "Los jugadores deben ser diferentes") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, message = "Creando nueva partida...") }
            try {
                val partida = Game(
                    Gameid = 0,
                    Fecha = LocalDate.now().toString(),
                    JugadorId1 = jugador1.Jugadorid,
                    JugadorId2 = jugador2.Jugadorid,
                    GanadorId = null,
                    EsFinalizada = false,
                    tablero = "",
                    jugadorActual = PlayerSymbol.X.symbol
                )

                val partidaCreada = postPartidaUseCase(partida)
                partidaId = partidaCreada?.Gameid

                _state.update {
                    it.copy(
                        board = List(9) { null },
                        currentPlayer = PlayerSymbol.X,
                        winner = null,
                        isDraw = false,
                        gameStarted = true,
                        isLoading = false,
                        message = "Partida $partidaId - ${jugador1.Nombres} vs ${jugador2.Nombres}"
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        message = "Error al crear partida: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onCellClick(index: Int) {
        if (_state.value.board[index] != null || _state.value.winner != null) {
            return
        }

        val newBoard = _state.value.board.toMutableList()
        newBoard[index] = _state.value.currentPlayer

        val newWinner = checkWinner(newBoard)
        val isDraw = newBoard.all { it != null } && newWinner == null

        guardarMovimientoEnApi(index)

        _state.update {
            it.copy(
                board = newBoard,
                currentPlayer = if (it.currentPlayer == PlayerSymbol.X) PlayerSymbol.O else PlayerSymbol.X,
                winner = newWinner,
                isDraw = isDraw
            )
        }

    }

    private fun guardarMovimientoEnApi(posicion: Int) {
        viewModelScope.launch {
            try {
                partidaId?.let { id ->
                    val fila = (posicion / 3) + 1
                    val columna = (posicion % 3) + 1

                    val movimiento = Movimientos(
                        partidaId = id,
                        jugador = _state.value.currentPlayer.symbol,
                        posicionFila = fila,
                        posicionColumna = columna,
                    )

                    val resultado = postMovimientoUseCase(movimiento)
                    if (!resultado) {
                        _state.update { it.copy(message = "Error al guardar movimiento") }
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(message = "Error API: ${e.message}") }
            }
        }
    }

    fun restartGame() {
        partidaId = null
        _state.update {
            it.copy(
                board = List(9) { null },
                currentPlayer = PlayerSymbol.X,
                winner = null,
                isDraw = false,
                gameStarted = false,
                message = "Juego reiniciado"
            )
        }
    }
    private fun reconstruirTableroDesdeMovimientos(movimientos: List<Movimientos>): List<PlayerSymbol?> {
        val tablero = MutableList(9) { null as PlayerSymbol? }

        movimientos.forEach { movimiento ->
            val fila = movimiento.posicionFila
            val columna = movimiento.posicionColumna
            val posicion = (fila - 1) * 3 + (columna - 1)
            val jugador = when (movimiento.jugador.uppercase()) {
                "X" -> PlayerSymbol.X
                "O" -> PlayerSymbol.O
                else -> null
            }

            if (jugador != null && posicion in 0..8) {
                tablero[posicion] = jugador
            }
        }

        return tablero
    }

    private fun determinarJugadorActual(movimientos: List<Movimientos>): PlayerSymbol {
        return if (movimientos.isEmpty()) {
            PlayerSymbol.X
        } else if (movimientos.size % 2 == 0) {
            PlayerSymbol.X
        } else {
            PlayerSymbol.O
        }
    }

    private fun checkWinner(board: List<PlayerSymbol?>): PlayerSymbol? {
        val winningLines = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
            listOf(0, 4, 8), listOf(2, 4, 6)
        )

        for (line in winningLines) {
            val (a, b, c) = line
            if (board[a] != null && board[a] == board[b] && board[a] == board[c]) {
                return board[a]
            }
        }
        return null
    }
}

data class ApiGameUiState(
    val movimientos: List<Movimientos> = emptyList(),
    val partidaIdInput: String = "",
    val jugadores: List<Player> = emptyList(),
    val jugador1Seleccionado: Player? = null,
    val jugador2Seleccionado: Player? = null,
    val board: List<PlayerSymbol?> = List(9) { null },
    val currentPlayer: PlayerSymbol = PlayerSymbol.X,
    val winner: PlayerSymbol? = null,
    val isDraw: Boolean = false,
    val gameStarted: Boolean = false,
    val message: String? = null,
    val isLoading: Boolean = false
)

enum class PlayerSymbol(val symbol: String) {
    X("X"),
    O("O")
}