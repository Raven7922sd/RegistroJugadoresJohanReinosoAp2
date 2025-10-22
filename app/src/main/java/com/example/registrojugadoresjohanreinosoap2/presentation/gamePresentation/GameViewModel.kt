package com.example.registrojugadoresjohanreinosoap2.presentation.gamePresentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registrojugadoresjohanreinosoap2.domain.model.Game
import com.example.registrojugadoresjohanreinosoap2.domain.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()
    private var partidaId: Int? = null


    fun cargarPartida(id: Int) {
        viewModelScope.launch {
            try {
                val partida = gameRepository.getGame(id)
                partida?.let { p ->
                    partidaId = id
                    _state.update { currentState ->
                        currentState.copy(
                            board = deserializarTablero(p.tablero),
                            currentPlayer = if (p.jugadorActual == "X") Player.X else Player.O,
                            gameStarted = true,
                            playerSelection = if (p.jugadorActual == "X") Player.X else Player.O
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(message = "Error al cargar partida") }
            }
        }
    }

    fun selectPlayer(player: Player) {
        _state.update { it.copy(playerSelection = player) }
    }

    fun startGame() {
        viewModelScope.launch {
            try {
                if (_state.value.playerSelection != null) {
                    val partida = Game(
                        Gameid = 0,
                        Fecha = LocalDate.now().toString(),
                        JugadorId1 = 1,
                        JugadorId2 = 2,
                        GanadorId = null,
                        EsFinalizada = false,
                        tablero = serializarTablero(_state.value.board),
                        jugadorActual = _state.value.currentPlayer.symbol
                    )
                    partidaId = gameRepository.upsert(partida)
                    _state.update { it.copy(gameStarted = true) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(message = "Error al iniciar partida") }
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

        _state.update {
            it.copy(
                board = newBoard,
                currentPlayer = if (it.currentPlayer == Player.X) Player.O else Player.X,
                winner = newWinner,
                isDraw = isDraw
            )
        }
        guardarMovimiento()

        if (newWinner != null || isDraw) {
            finalizarPartida()
        }
    }

    private fun guardarMovimiento() {
        viewModelScope.launch {
            try {
                partidaId?.let { id ->
                    val partida = Game(
                        Gameid = id,
                        Fecha = LocalDate.now().toString(),
                        JugadorId1 = 1,
                        JugadorId2 = 2,
                        GanadorId = null,
                        EsFinalizada = false,
                        tablero = serializarTablero(_state.value.board),
                        jugadorActual = _state.value.currentPlayer.symbol
                    )
                    gameRepository.upsert(partida)
                }
            } catch (e: Exception) {
                _state.update { it.copy(message = "Error al guardar movimiento") }
            }
        }
    }

    private fun finalizarPartida() {
        viewModelScope.launch {
            try {
                partidaId?.let { id ->
                    val currentState = _state.value
                    val partida = Game(
                        Gameid = id,
                        Fecha = LocalDate.now().toString(),
                        JugadorId1 = 1,
                        JugadorId2 = 2,
                        GanadorId = when (currentState.winner) {
                            Player.X -> 1
                            Player.O -> 2
                            null -> null
                        },
                        EsFinalizada = true,
                        tablero = serializarTablero(currentState.board),
                        jugadorActual = currentState.currentPlayer.symbol
                    )
                    gameRepository.upsert(partida)
                    partidaId = null
                    _state.update { it.copy(message = "Partida guardada") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(message = "Error al guardar: ${e.message}") }
            }
        }
    }

    fun restartGame() {
        partidaId = null
        _state.update {
            it.copy(
                board = List(9) { null },
                currentPlayer = it.playerSelection ?: Player.X,
                winner = null,
                isDraw = false,
                gameStarted = false
            )
        }
    }

    private fun serializarTablero(board: List<Player?>): String {
        val tableroCompleto = if (board.size < 9) {
            board + List(9 - board.size) { null }
        } else {
            board.take(9)
        }

        return tableroCompleto.joinToString(",") { it?.symbol ?: " " }
    }

    private fun deserializarTablero(tableroString: String): List<Player?> {
        val elementos = tableroString.split(",")
        return (0 until 9).map { index ->
            if (index < elementos.size) {
                when (elementos[index].trim()) {
                    "X" -> Player.X
                    "O" -> Player.O
                    else -> null
                }
            } else {
                null
            }
        }
    }

    private fun checkWinner(board: List<Player?>): Player? {
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

data class GameUiState(
    val board: List<Player?> = List(9) { null },
    val currentPlayer: Player = Player.X,
    val winner: Player? = null,
    val isDraw: Boolean = false,
    val playerSelection: Player? = null,
    val gameStarted: Boolean = false,
    val message: String? = null
)

enum class Player(val symbol: String) {
    X("X"),
    O("O")
}