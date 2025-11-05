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
            JugadorSelectorBottomSheet(
                jugadores = state.jugadoresDisponibles,
                onJugadorSelected = { jugador ->
                    when (selectorType) {
                        "jugador1" -> onEvent(EditGameUiEvent.JugadorId1Changed(jugador.remoteId ?: 0))
                        "jugador2" -> onEvent(EditGameUiEvent.JugadorId2Changed(jugador.remoteId ?: 0))
                    }
                    showJugadorSelector = false
                }
            )
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
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
                    else "Jugador 1: ${state.jugadoresDisponibles.find { it.remoteId == state.jugadorId1 }?.Nombres ?: "ID ${state.jugadorId1}"}",
                    modifier = Modifier.weight(1f)
                )
            }
            if (state.jugadorId1error != null) {
                Text(
                    state.jugadorId1error,
                    color = MaterialTheme.colorScheme.error
                )
            }

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
                    else "Jugador 2: ${state.jugadoresDisponibles.find { it.remoteId == state.jugadorId2 }?.Nombres ?: "ID ${state.jugadorId2}"}",
                    modifier = Modifier.weight(1f)
                )
            }
            if (state.jugadorId2error != null) {
                Text(
                    state.jugadorId2error,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = state.fecha,
                onValueChange = { onEvent(EditGameUiEvent.FechaChanged(it)) },
                label = { Text("Fecha") },
                isError = state.fechaError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (state.fechaError != null) {
                Text(
                    state.fechaError,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(Modifier.height(16.dp))

            if (state.jugadorId1 > 0 && state.jugadorId2 > 0) {
                val jugador1 = state.jugadoresDisponibles.find { it.remoteId == state.jugadorId1 }
                val jugador2 = state.jugadoresDisponibles.find { it.remoteId == state.jugadorId2 }

                Text("Ganador:", style = MaterialTheme.typography.labelMedium)
                Spacer(Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                    FilterChip(
                        selected = state.ganadorId == state.jugadorId1,
                        onClick = { onEvent(EditGameUiEvent.GanadorIdChanged(state.jugadorId1)) },
                        label = { Text(jugador1?.Nombres ?: "Jugador 1") }
                    )

                    Spacer(Modifier.width(8.dp))

                    FilterChip(
                        selected = state.ganadorId == state.jugadorId2,
                        onClick = { onEvent(EditGameUiEvent.GanadorIdChanged(state.jugadorId2)) },
                        label = { Text(jugador2?.Nombres ?: "Jugador 2") }
                    )

                    Spacer(Modifier.width(8.dp))

                    FilterChip(
                        selected = state.ganadorId == null,
                        onClick = { onEvent(EditGameUiEvent.GanadorIdChanged(null)) },
                        label = { Text("Empate") }
                    )
                }
                Spacer(Modifier.height(16.dp))
            }

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
                ) { Text("Guardar") }

                Spacer(Modifier.width(8.dp))

                if (!state.isNew) {
                    OutlinedButton(
                        onClick = { onEvent(EditGameUiEvent.Delete) },
                        enabled = !state.isDeleting,
                        modifier = Modifier.weight(1f)
                    ) { Text("Eliminar") }
                }
            }
        }
    }
}

@Composable
fun JugadorSelectorBottomSheet(
    jugadores: List<Player>,
    onJugadorSelected: (Player) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Seleccione un Jugador", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(jugadores) { jugador ->
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

@Preview
@Composable
private fun EditPartidaBodyPreview() {
    val state = EditGameUiState()
    MaterialTheme {
        EditGameBody(state = state) { _ -> }
    }
}