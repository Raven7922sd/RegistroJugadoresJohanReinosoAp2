package com.example.registrojugadoresjohanreinosoap2.presentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.registrojugadoresjohanreinosoap2.domain.model.Player

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerListScreen(
    onNavigateToEdit: (Int) -> Unit,
    onNavigateToCreate: () -> Unit,
    viewModel: ListPlayerViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Lista de Jugadores",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF7E57C2

                    )
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigateToCreate() }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir jugador")
            }
        }
    ) { paddingValues ->
        PlayerListContent(
            state = state,
            onEvent = { event ->
                when (event) {
                    is ListPlayerUiEvent.Edit -> onNavigateToEdit(event.id)
                    is ListPlayerUiEvent.Delete -> viewModel.onEvent(event)
                    else -> Unit
                }
            },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun PlayerListContent(
    state: ListPlayerUiState,
    onEvent: (ListPlayerUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var playerToDelete by remember { mutableStateOf<Player?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            state.isLoading -> CircularProgressIndicator()
            state.players.isEmpty() -> Text(
                text = "No hay jugadores registrados",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
            else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.players, key = { it.Jugadorid }) { player ->
                    PlayerCard(
                        player = player,
                        onClick = { onEvent(ListPlayerUiEvent.Edit(player.Jugadorid)) },
                        onDelete = { playerToDelete = player }
                    )
                }
            }
        }

        if (playerToDelete != null) {
            AlertDialog(
                onDismissRequest = { playerToDelete = null },
                title = { Text("Eliminar jugador") },
                text = { Text("¿Estás seguro de eliminar a este jugador?") },
                confirmButton = {
                    TextButton(onClick = {
                        onEvent(ListPlayerUiEvent.Delete(playerToDelete!!.Jugadorid))
                        playerToDelete = null
                    }) {
                        Text("Sí")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { playerToDelete = null }) {
                        Text("No")
                    }
                }
            )
        }
    }
}

@Composable
private fun PlayerCard(
    player: Player,
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
                Text("Nombre: ${player.Nombres}")
                Text("Partidas: ${player.Partidas}")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Borrar")
            }
        }
    }
}
