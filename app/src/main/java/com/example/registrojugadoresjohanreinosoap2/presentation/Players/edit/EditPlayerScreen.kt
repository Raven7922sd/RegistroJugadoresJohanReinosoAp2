package com.example.registrojugadoresjohanreinosoap2.presentation.Players.edit

import androidx.compose.foundation.BorderStroke
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

@Composable
fun EditPlayerScreen(
    playerId: String?,viewModel: EditPlayerViewModel = hiltViewModel()
) {
    LaunchedEffect(playerId) {
        viewModel.onEvent(EditPlayerUiEvent.Load(playerId))
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
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
                        text = if (state.isNew) "Nuevo Jugador" else "Editar Jugador",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .fillMaxSize()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Información del Jugador",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = state.name,
                        onValueChange = { onEvent(EditPlayerUiEvent.NameChanged(it)) },
                        label = { Text("Nombre") },
                        isError = state.nameError != null,
                        supportingText = {
                            if (state.nameError != null) {
                                Text(
                                    text = state.nameError,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value = state.gamesPlayed?.toString() ?: "",
                        onValueChange = { onEvent(EditPlayerUiEvent.PartidaChanged(it)) },
                        label = { Text("Partidas Jugadas") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = state.gamesPlayedError != null,
                        supportingText = {
                            if (state.gamesPlayedError != null) {
                                Text(
                                    text = state.gamesPlayedError,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { onEvent(EditPlayerUiEvent.Save) },
                    enabled = !state.isSaving && !state.isDeleting,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Text(
                        text = if (state.isSaving) "Guardando" else "Guardar",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }

                if (!state.isNew) {
                    Spacer(Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = { onEvent(EditPlayerUiEvent.Delete) },
                        enabled = !state.isDeleting && !state.isSaving,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (state.isDeleting || state.isSaving)
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            else
                                MaterialTheme.colorScheme.error
                        )
                    ) {
                        if (state.isDeleting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.error,
                                strokeWidth = 2.dp
                            )
                            Spacer(Modifier.width(8.dp))
                        }
                        Text(
                            text = if (state.isDeleting) "Eliminando" else "Eliminar Jugador",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditPlayerBodyNewPreview() {
    val state = EditPlayerUiState(
        isNew = true,
        name = "",
        gamesPlayed = null
    )
    MaterialTheme {
        EditPlayerBody(state = state) {}
    }
}

@Preview(showBackground = true)
@Composable
private fun EditPlayerBodyEditPreview() {
    val state = EditPlayerUiState(
        isNew = false,
        name = "Alberto",
        gamesPlayed = 15
    )
    MaterialTheme {
        EditPlayerBody(state = state) {}
    }
}

@Preview(showBackground = true)
@Composable
private fun EditPlayerBodyWithErrorsPreview() {
    val state = EditPlayerUiState(
        isNew = false,
        name = "",
        gamesPlayed = null,
        nameError = "El nombre es requerido",
        gamesPlayedError = "Debe ingresar un número válido"
    )
    MaterialTheme {
        EditPlayerBody(state = state) {}
    }
}