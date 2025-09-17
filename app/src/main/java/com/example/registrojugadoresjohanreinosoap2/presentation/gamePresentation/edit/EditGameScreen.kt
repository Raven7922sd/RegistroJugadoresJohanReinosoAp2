package com.example.registrojugadoresjohanreinosoap2.presentation.gamePresentation.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.registrojugadoresjohanreinosoap2.domain.model.Player // Asegúrate que esta importación sea correcta

@Composable
fun EditGameScreen(
    gameId: Int?,
    navController: NavController,
    viewModel: EditGameViewModel = hiltViewModel()
) {
    LaunchedEffect(gameId) {
        viewModel.onEvent(EditGameUiEvent.Load(gameId))
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isGameSaved, state.isGameDeleted) {
        if (state.isGameSaved || state.isGameDeleted) {
            navController.popBackStack()
        }
    }

    EditGameBody(state, viewModel::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditGameBody(
    state: EditGameUiState,
    onEvent: (EditGameUiEvent) -> Unit
) {
    var showJugadorSelector by remember { mutableStateOf(false) }
    var selectorType by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState()

    if (showJugadorSelector) {
        ModalBottomSheet(
            onDismissRequest = { showJugadorSelector = false },
            sheetState = sheetState
        ) {
            JugadorSelectorBottomSheetContent(
                jugadores = state.jugadores,
                onJugadorSelected = { jugador ->
                    when (selectorType) {
                        "jugador1" -> onEvent(EditGameUiEvent.JugadorId1Changed(jugador.Jugadorid))
                        "jugador2" -> onEvent(EditGameUiEvent.JugadorId2Changed(jugador.Jugadorid))
                    }
                    showJugadorSelector = false
                }
            )
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (state.isNew) "Nueva Partida" else "Editar Partida",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF7E57C2)
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            OutlinedButton(
                onClick = {
                    selectorType = "jugador1"
                    showJugadorSelector = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (state.jugadorId1 == 0) "Seleccionar Jugador 1"
                    else "Jugador 1: ${state.jugadores.find { it.Jugadorid == state.jugadorId1 }?.Nombres ?: "ID ${state.jugadorId1}"}"
                )
            }
            state.jugadorId1error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    selectorType = "jugador2"
                    showJugadorSelector = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (state.jugadorId2 == 0) "Seleccionar Jugador 2"
                    else "Jugador 2: ${state.jugadores.find { it.Jugadorid == state.jugadorId2 }?.Nombres ?: "ID ${state.jugadorId2}"}"
                )
            }
            state.jugadorId2error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = state.fecha,
                onValueChange = { onEvent(EditGameUiEvent.FechaChanged(it)) },
                label = { Text("Fecha de la Partida") },
                isError = state.fechaError != null,
                modifier = Modifier.fillMaxWidth()
            )
            state.fechaError?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            Spacer(Modifier.height(16.dp))

            if (state.jugadorId1 != 0 && state.jugadorId2 != 0 && state.jugadorId1 != state.jugadorId2) {
                val jugador1 = state.jugadores.find { it.Jugadorid == state.jugadorId1 }
                val jugador2 = state.jugadores.find { it.Jugadorid == state.jugadorId2 }

                Text("Ganador:", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                    jugador1?.let {
                        FilterChip(
                            selected = state.ganadorId == it.Jugadorid,
                            onClick = { onEvent(EditGameUiEvent.GanadorIdChanged(it.Jugadorid)) },
                            label = { Text(it.Nombres) }
                        )
                    }
                    jugador2?.let {
                        FilterChip(
                            selected = state.ganadorId == it.Jugadorid,
                            onClick = { onEvent(EditGameUiEvent.GanadorIdChanged(it.Jugadorid)) },
                            label = { Text(it.Nombres) }
                        )
                    }
                    FilterChip(
                        selected = state.ganadorId == 0,
                        onClick = { onEvent(EditGameUiEvent.GanadorIdChanged(null)) },
                        label = { Text("Empate") }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = state.esFinalizada,
                    onCheckedChange = { onEvent(EditGameUiEvent.EsFinalizadaChanged(it)) }
                )
                Spacer(Modifier.width(8.dp))
                Text("Partida finalizada")
            }

            Spacer(Modifier.height(16.dp))

            Row {
                Button(
                    onClick = { onEvent(EditGameUiEvent.Save) },
                    enabled = !state.isSaving,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Guardar")
                }

                Spacer(Modifier.width(8.dp))

                if (!state.isNew) {
                    OutlinedButton(
                        onClick = { onEvent(EditGameUiEvent.Delete) },
                        enabled = !state.isDeleting,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Eliminar")
                    }
                }
            }
        }
    }
}

@Composable
fun JugadorSelectorBottomSheetContent(
    jugadores: List<Player>,
    onJugadorSelected: (Player) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Seleccionar Jugador", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        if (jugadores.isEmpty()) {
            Text("No hay jugadores disponibles")
        } else {
            LazyColumn {
                items(jugadores, key = { it.Jugadorid }) { jugador ->
                    Button(
                        onClick = { onJugadorSelected(jugador) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(jugador.Nombres)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditGameScreenPreview() {
    MaterialTheme {
        val samplePlayers = listOf(
            Player(Jugadorid = 1, Nombres = "Jugador 1", Partidas = 5),
            Player(Jugadorid = 2, Nombres = "Jugador 2", Partidas = 3)
        )

        val sampleState = EditGameUiState(
            id = 1,
            fecha = "2023-10-01",
            jugadorId1 = 1,
            jugadorId2 = 2,
            ganadorId = 1,
            isNew = false,
            esFinalizada = true,
            jugadores = samplePlayers
        )
        EditGameBody(state = sampleState, onEvent = {})
    }
}
