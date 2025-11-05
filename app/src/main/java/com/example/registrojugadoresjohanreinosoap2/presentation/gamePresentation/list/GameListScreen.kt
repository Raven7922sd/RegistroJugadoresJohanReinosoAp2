package com.example.registrojugadoresjohanreinosoap2.presentation.gamePresentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.registrojugadoresjohanreinosoap2.domain.model.Game

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameListScreen(
    onNavigateToEdit: (Int) -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToPlayers: () -> Unit,
    onNavigateToGame: () -> Unit,
    onContinueGame: (Int) -> Unit,
    viewModel: ListGameViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    PartidaListBody(
        state = state,
        onNavigateToPlayers = onNavigateToPlayers,
        onNavigateToGame = onNavigateToGame,
        onContinueGame = onContinueGame,
        onEvent = { event ->
            when (event) {
                is ListGameUiEvent.Edit -> onNavigateToEdit(event.id)
                is ListGameUiEvent.CreateNew -> onNavigateToCreate()
                is ListGameUiEvent.NavigateToGame -> onNavigateToGame()
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@Composable
private fun PartidaListBody(
    state: ListGameUiState,
    onNavigateToPlayers: () -> Unit,
    onNavigateToGame: () -> Unit,
    onContinueGame: (Int) -> Unit,
    onEvent: (ListGameUiEvent) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            Row {
                FloatingActionButton(onClick = onNavigateToGame) {
                    Text("+")
                }
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
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                val partidasEnCurso = state.games.filter { !it.EsFinalizada }
                if (partidasEnCurso.isNotEmpty()) {
                    item {
                        Text(
                            "Partidas en Curso",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                    items(partidasEnCurso) { partida ->
                        PartidaCard(
                            partida = partida,
                            onClick = { onContinueGame(partida.Gameid) },
                            onDelete = { onEvent(ListGameUiEvent.Delete(partida.Gameid)) }
                        )
                    }
                }
                val partidasFinalizadas = state.games.filter { it.EsFinalizada }
                if (partidasFinalizadas.isNotEmpty()) {
                    item {
                        Text(
                            "Partidas Finalizadas",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                    items(partidasFinalizadas) { partida ->
                        PartidaCard(
                            partida = partida,
                            onClick = { onEvent(ListGameUiEvent.Edit(partida.Gameid)) },
                            onDelete = { onEvent(ListGameUiEvent.Delete(partida.Gameid)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PartidaCard(
    partida: Game,
    onClick: (Game) -> Unit,
    onDelete: (Int) -> Unit,
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
                    text = if (esPartidaEnCurso) "En juego..." else "Finalizada",
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
                        "Ganador: ${partida.GanadorId}"
                    else
                        "Empate",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (partida.GanadorId != null) Color(0xFF0066CC) else Color.Gray
                )
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (esPartidaEnCurso) {
                    Button(
                        onClick = { onClick(partida) },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Continuar")
                    }
                }
                IconButton(onClick = { onDelete(partida.Gameid) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar partida")
                }
            }
        }
    }
}

@Preview
@Composable
private fun PartidaListBodyPreview() {
    MaterialTheme {
        val state = ListGameUiState(
            games = listOf(
                Game(
                    Gameid = 1,
                    Fecha = "27/3/2025",
                    JugadorId1 = 1,
                    JugadorId2 = 2,
                    GanadorId = 101,
                    EsFinalizada = true
                ),
                Game(
                    Gameid = 2,
                    Fecha = "16/01/2024",
                    JugadorId1 = 3,
                    JugadorId2 = 4,
                    GanadorId = null,
                    EsFinalizada = true
                ),
                Game(
                    Gameid = 3,
                    Fecha = "16/01/2024",
                    JugadorId1 = 5,
                    JugadorId2 = 6,
                    GanadorId = null,
                    EsFinalizada = true
                ),
            )
        )
       PartidaListBody(
            state = state,
            onNavigateToPlayers = {},
            onNavigateToGame = {},
            onContinueGame = {},
            onEvent = {}
        )
    }
}