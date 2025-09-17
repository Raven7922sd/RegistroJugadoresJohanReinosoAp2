package com.example.registrojugadoresjohanreinosoap2.presentation.gamePresentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
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
    onNavigateGameEdit: (Int) -> Unit,
    onNavigateGameCreate: () -> Unit,
    onOpenDrawer: () -> Unit,
    viewModel: ListGameViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Lista de Partidas",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menú",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF7E57C2)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigateGameCreate() }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir partida")
            }
        }


    ) { paddingValues ->
        GameListContent(
            state = state,
            onEvent = { event ->
                when (event) {
                    is ListGameUiEvent.Edit -> onNavigateGameEdit(event.id)
                    is ListGameUiEvent.Delete -> viewModel.onEvent(event)
                    else -> Unit
                }
            },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun GameListContent(
    state: ListGameUiState,
    onEvent: (ListGameUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var gameToDelete by remember { mutableStateOf<Game?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            state.isLoading -> CircularProgressIndicator()
            state.games.isEmpty() -> Text(
                text = "No hay partidas registradas",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
            else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.games, key = { it.Gameid }) { game ->
                    GameCard(
                        game = game,
                        onClick = { onEvent(ListGameUiEvent.Edit(game.Gameid)) },
                        onDelete = { gameToDelete = game }
                    )
                }
            }
        }

        if (gameToDelete != null) {
            AlertDialog(
                onDismissRequest = { gameToDelete = null },
                title = { Text("Eliminar partida") },
                text = { Text("¿Estás seguro de eliminar esta partida?") },
                confirmButton = {
                    TextButton(onClick = {
                        onEvent(ListGameUiEvent.Delete(gameToDelete!!.Gameid))
                        gameToDelete = null
                    }) {
                        Text("Sí")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { gameToDelete = null }) {
                        Text("No")
                    }
                }
            )
        }
    }
}

@Composable
private fun GameCard(
    game: Game,
    onClick: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Fecha: ${game.Fecha}")
                Text(text = "Jugador 1: ${game.JugadorId1}")
                Text(text = "Jugador 2: ${game.JugadorId2}")
                Text(text = "Ganador: ${game.GanadorId}")
                Text("Finalizada: ${if (game.EsFinalizada) "Sí" else "No"}")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Borrar")
            }
        }
    }
}

@Composable
@Preview
private fun GameCardPreview() {
    GameCard(
        game = Game(1, "2023-10-10", 3, 4, 3, true),
        onClick = {},
        onDelete = {}
    )
}