package com.example.registrojugadoresjohanreinosoap2.presentation.apiPresentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.registrojugadoresjohanreinosoap2.domain.model.Game

@Composable
fun ListPartidaApiScreen(
    onNavigateToCreate: () -> Unit,
    onNavigateToGame: (Int?) -> Unit,
    viewModel: ListPartidaApiViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events by viewModel.eventFlow.collectAsStateWithLifecycle()

    LaunchedEffect(events) {
        when (val event = events) {
            is ListPartidaApiUiEvent.NavigateToCreate -> {
                onNavigateToCreate()
                viewModel.onEventConsumed()
            }
            is ListPartidaApiUiEvent.NavigateToGame -> {
                onNavigateToGame(event.partidaId)
                viewModel.onEventConsumed()
            }
            else -> {}
        }
    }

    PartidaListApiBody(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun PartidaListApiBody(
    state: ListPartidaApiUiState,
    onEvent: (ListPartidaApiUiEvent) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(ListPartidaApiUiEvent.NavigateToCreate) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear partida")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    state.message?.let { message ->
                        item {
                            Text(
                                text = message,
                                color = if (message.contains("Error")) Color.Red else Color.Green,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }

                    val partidasEnCurso = state.partidas.filter { !it.EsFinalizada }
                    if (partidasEnCurso.isNotEmpty()) {
                        item {
                            Text(
                                "Partidas en Curso",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                        }
                        items(partidasEnCurso) { partida ->
                            PartidaApiCard(
                                partida = partida,
                                onClick = { onEvent(ListPartidaApiUiEvent.NavigateToGame(partida.Gameid)) }
                            )
                        }
                    }

                    val partidasFinalizadas = state.partidas.filter { it.EsFinalizada }
                    if (partidasFinalizadas.isNotEmpty()) {
                        item {
                            Text(
                                "Partidas Finalizadas",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                        }
                        items(partidasFinalizadas) { partida ->
                            PartidaApiCard(
                                partida = partida,
                                onClick = { onEvent(ListPartidaApiUiEvent.NavigateToGame(partida.Gameid)) }
                            )
                        }
                    }

                    if (state.partidas.isEmpty() && !state.isLoading) {
                        item {
                            Text(
                                "No hay partidas disponibles",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PartidaApiCard(
    partida: Game,
    onClick: (Game) -> Unit
) {
    val esPartidaEnCurso = !partida.EsFinalizada
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick(partida) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Partida #${partida.Gameid}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = if (esPartidaEnCurso) "En juego" else "Finalizada",
                    color = if (esPartidaEnCurso) Color.Blue else Color.Green
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Jugadores: ${partida.JugadorId1} vs ${partida.JugadorId2}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Fecha: ${partida.Fecha}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(Modifier.height(8.dp))

            if (!esPartidaEnCurso) {
                Text(
                    text = if (partida.GanadorId != null)
                        "Ganador: Jugador ${partida.GanadorId}"
                    else
                        "Empate",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (partida.GanadorId != null) Color(0xFF0066CC) else Color.Gray
                )
            }

            Spacer(Modifier.height(8.dp))

            if (esPartidaEnCurso) {
                Button(
                    onClick = { onClick(partida) },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Continuar Partida")
                }
            }
        }
    }
}

@Preview
@Composable
private fun PartidaListApiPreview() {
    MaterialTheme {
        val state = ListPartidaApiUiState(
            partidas = listOf(
                Game(
                    Gameid = 1,
                    Fecha = "15/01/2024",
                    JugadorId1 = 7,
                    JugadorId2 = 8,
                    GanadorId = 7,
                    EsFinalizada = true
                ),
                Game(
                    Gameid = 2,
                    Fecha = "16/01/2024",
                    JugadorId1 = 9,
                    JugadorId2 = 10,
                    GanadorId = 10,
                    EsFinalizada = true
                ),
                Game(
                    Gameid = 3,
                    Fecha = "17/01/2024",
                    JugadorId1 = 11,
                    JugadorId2 = 12,
                    GanadorId = 11,
                    EsFinalizada = false
                )
            )
        )
        PartidaListApiBody(
            state = state,
            onEvent = {}
        )
    }
}