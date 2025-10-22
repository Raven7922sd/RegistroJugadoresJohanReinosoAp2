package com.example.registrojugadoresjohanreinosoap2.presentation.apiPresentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle



@Composable
fun ApiGameScreen(
    onDrawer: () -> Unit = {},
    onNavigateBack: () -> Unit,
    partidaId: Int? = null,
    viewModel: GameApiViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(partidaId, viewModel) {
        if (partidaId != null) {
            viewModel.cargarPartida(partidaId)
        }
    }

    TicTacToeBody(
        state = state,
        viewModel = viewModel,
        onNavigateBack = onNavigateBack
    )
}

@Composable
private fun TicTacToeBody(
    state: ApiGameUiState,
    viewModel: GameApiViewModel,
    onNavigateBack: () -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            state.message?.let { message ->
                Text(
                    text = message,
                    color = when {
                        message.contains("Error") -> Color.Red
                        message.contains("¡") -> Color.Green
                        else -> Color.Blue
                    },
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(8.dp))
            }

            if (!state.gameStarted) {
                ApiPlayerSelectionScreen(
                    state = state,
                    viewModel = viewModel,


                )
            } else {
                ApiGameBoard(
                    uiState = state,
                    onCellClick = { viewModel.onCellClick(it) },
                    onRestartGame = { viewModel.restartGame() },
                    viewModel = viewModel,
                    onNavigateBack = onNavigateBack
                )
            }
        }
    }
}

@Composable
fun ApiPlayerSelectionScreen(
    state: ApiGameUiState,
    viewModel: GameApiViewModel
) {
    var showDialogJugador1 by remember { mutableStateOf(false) }
    var showDialogJugador2 by remember { mutableStateOf(false) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        Text("Selecciona los jugadores", fontSize = 20.sp, fontWeight = FontWeight.Medium)

        Button(
            onClick = { showDialogJugador1 = true },
            modifier = Modifier.fillMaxWidth(0.8f),
            enabled = state.jugadores.isNotEmpty()
        ) {
            Text(state.jugador1Seleccionado?.Nombres ?: "Seleccionar Jugador 1")
        }

        Button(
            onClick = { showDialogJugador2 = true },
            modifier = Modifier.fillMaxWidth(0.8f),
            enabled = state.jugadores.isNotEmpty() && state.jugador1Seleccionado != null
        ) {
            Text(state.jugador2Seleccionado?.Nombres ?: "Seleccionar Jugador 2")
        }

        Button(
            onClick = { viewModel.startGame() },
            enabled = state.jugador1Seleccionado != null && state.jugador2Seleccionado != null,
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Iniciar Partida", fontSize = 18.sp)
        }

        if (showDialogJugador1) {
            Dialog(
                onDismissRequest = { showDialogJugador1 = false }
            ) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight()
                ) {
                    Column {
                        Text(
                            "Jugador 1",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(16.dp)
                        )

                        Divider()

                        state.jugadores.forEach { jugador ->
                            Text(
                                jugador.Nombres,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.selectJugador1(jugador)
                                        showDialogJugador1 = false
                                    }
                                    .padding(16.dp),
                                fontSize = 16.sp
                            )
                            Divider()
                        }
                    }
                }
            }
        }

        if (showDialogJugador2) {
            val availableJugadores = state.jugadores.filter {
                it.Jugadorid != state.jugador1Seleccionado?.Jugadorid
            }

            Dialog(
                onDismissRequest = { showDialogJugador2 = false }
            ) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight()
                ) {
                    Column {
                        Text(
                            "Jugador 2",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(16.dp)
                        )

                        Divider()

                        if (availableJugadores.isEmpty()) {
                            Text(
                                "No hay más jugadores",
                                modifier = Modifier.padding(16.dp),
                                color = Color.Gray
                            )
                        } else {
                            availableJugadores.forEach { jugador ->
                                Text(
                                    jugador.Nombres,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.selectJugador2(jugador)
                                            showDialogJugador2 = false
                                        }
                                        .padding(16.dp),
                                    fontSize = 16.sp
                                )
                                Divider()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ApiGameBoard(
    uiState: ApiGameUiState,
    onCellClick: (Int) -> Unit,
    onRestartGame: () -> Unit,
    viewModel: GameApiViewModel,
    onNavigateBack: () -> Unit
) {
    val gameStatus = when {
        uiState.winner != null -> "El ganador es: ${uiState.winner.symbol}!"
        uiState.isDraw -> "Empate"
        else -> "Turno de: ${uiState.currentPlayer.symbol}"
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Cargar Partida por ID", fontSize = 20.sp, fontWeight = FontWeight.Medium)

        Row(
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = uiState.partidaIdInput,
                onValueChange = { viewModel.updatePartidaId(it) },
                label = { Text("ID de Partida") },
                placeholder = { Text("Ej: 1, 2, 3...") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )

            Button(
                onClick = { viewModel.cargarPartida()  },
                enabled = uiState.partidaIdInput.isNotBlank(),
                modifier = Modifier.height(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Refrescar")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Cargar")
            }
        }

        Text(text = gameStatus, fontSize = 24.sp, fontWeight = FontWeight.Bold)

        if (uiState.jugador1Seleccionado != null && uiState.jugador2Seleccionado != null) {
            Text(
                text = "${uiState.jugador1Seleccionado.Nombres} (X) vs ${uiState.jugador2Seleccionado.Nombres} (O)",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }

        GameBoardBodyApi(
            board = uiState.board.map { it?.let { playerSymbol ->
                when (playerSymbol) {
                    PlayerSymbol.X -> PlayerSymbol.X
                    PlayerSymbol.O -> PlayerSymbol.O
                }
            }},
            onCellClick = onCellClick
        )

        Button(onClick = onRestartGame) {
            Text("Volver al Inicio", fontSize = 18.sp)
        }
    }
}

@Composable
fun GameBoardBodyApi(board: List<PlayerSymbol?>, onCellClick: (Int) -> Unit) {
    Column {
        (0..2).forEach { row ->
            Row {
                (0..2).forEach { col ->
                    val index = row * 3 + col
                    BoardCell(board[index]) {
                        onCellClick(index)
                    }
                }
            }
        }
    }
}

@Composable
private fun BoardCell(
    player: PlayerSymbol?,
    onCellClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .padding(4.dp)
            .background(Color.LightGray)
            .clickable { onCellClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = player?.symbol ?: "",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = if (player == PlayerSymbol.X) Color.Blue else Color.Red
        )
    }
}
