package com.example.registrojugadoresjohanreinosoap2.presentation.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPlayerScreen(
    playerId: Int?,
    navController: NavController,
    viewModel: EditPlayerViewModel = hiltViewModel()
) {
    LaunchedEffect(playerId) {
        viewModel.onEvent(EditPlayerUiEvent.Load(playerId))
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isSaving) {
        if (state.isSaving) {
            navController.popBackStack()
        }
    }

    EditPlayerBody(state, viewModel::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditPlayerBody(
    state: EditPlayerUiState,
    onEvent: (EditPlayerUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Datos de jugador",
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
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = { onEvent(EditPlayerUiEvent.NameChanged(it)) },
                label = { Text("Nombre") },
                isError = state.nameError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (state.nameError != null) {
                Text(
                    state.nameError,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = state.gamesPlayed?.toString() ?: "",
                onValueChange = { onEvent(EditPlayerUiEvent.PartidaChanged(it)) },
                label = { Text("Partida") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = state.gamesPlayedError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (state.gamesPlayedError != null) {
                Text(
                    state.gamesPlayedError,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(Modifier.height(16.dp))

            Row {
                Button(
                    onClick = { onEvent(EditPlayerUiEvent.Save) },
                    enabled = !state.isSaving,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar")
                }
                Spacer(Modifier.fillMaxWidth())
                if (!state.isNew) {
                    OutlinedButton(
                        onClick = { onEvent(EditPlayerUiEvent.Delete) },
                        enabled = !state.isDeleting
                    ) {
                        Text("Eliminar")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun EditPlayerBodyPreview() {
    val state = EditPlayerUiState()
    MaterialTheme {
        EditPlayerBody(state = state) {}
    }
}
