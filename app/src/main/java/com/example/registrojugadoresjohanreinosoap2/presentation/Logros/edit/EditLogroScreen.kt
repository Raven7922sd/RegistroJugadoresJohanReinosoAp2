package edu.ucne.registrojugadores.Presentation.Logros.Edit

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun EditLogroScreen(
    logroId: Int?,
    onSaveComplete: () -> Unit = {},
    onDeleteComplete: () -> Unit = {},
    viewModel: EditLogroViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.saved) {
        if (state.saved) {
            onSaveComplete()
        }
    }

    LaunchedEffect(state.deleted) {
        if (state.deleted) {
            onDeleteComplete()
        }
    }

    LaunchedEffect(logroId) {
        viewModel.onEvent(EditLogroUiEvent.Load(logroId))
    }

    EditLogroBody(state, viewModel::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditLogroBody(
    state: EditLogroUiState,
    onEvent: (EditLogroUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (state.isNew) "Nuevo Logro"
                        else "Editar Logro"
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = state.NombreLogro,
                onValueChange = { onEvent(EditLogroUiEvent.NombreChanged(it)) },
                label = { Text("Nombre del logro *") },
                placeholder = { Text("Ej: Primer Victoria") },
                isError = state.nombreError != null,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            if (state.nombreError != null) {
                Text(
                    text = state.nombreError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = state.Descripcion,
                onValueChange = { onEvent(EditLogroUiEvent.DescripcionChanged(it)) },
                label = { Text("Descripción *") },
                placeholder = { Text("Ej: Ganar tu primera partida de Tres en Raya") },
                isError = state.descripcionError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 4
            )
            if (state.descripcionError != null) {
                Text(
                    text = state.descripcionError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (state.EsDesbloqueado) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    }
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Estado del Logro",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilterChip(
                            selected = !state.EsDesbloqueado,
                            onClick = { onEvent(EditLogroUiEvent.esLogradoChanged(false)) },
                            label = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "No logrado",
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    Text("No Logrado")
                                }
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.errorContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onErrorContainer
                            )
                        )

                        FilterChip(
                            selected = state.EsDesbloqueado,
                            onClick = { onEvent(EditLogroUiEvent.esLogradoChanged(true)) },
                            label = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = "Logrado",
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    Text("Logrado")
                                }
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = if (state.EsDesbloqueado)
                            "✅ Este logro ha sido completado"
                        else
                            "⏳ Este logro está pendiente",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (state.EsDesbloqueado)
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (!state.isNew) {
                    OutlinedButton(
                        onClick = { onEvent(EditLogroUiEvent.Delete) },
                        enabled = !state.isDeleting,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        if (state.isDeleting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Eliminando...")
                        } else {
                            Text("Eliminar")
                        }
                    }
                    Spacer(Modifier.width(16.dp))
                } else {
                    Spacer(Modifier.weight(1f))
                }

                Button(
                    onClick = { onEvent(EditLogroUiEvent.Save) },
                    enabled = !state.isSaving && state.NombreLogro.isNotBlank() && state.Descripcion.isNotBlank(),
                    modifier = Modifier.weight(1f)
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Guardando...")
                    } else {
                        Text(if (state.isNew) "Crear" else "Actualizar")
                    }
                }
            }

            state.nombreError?.takeIf { it.contains("Error") }?.let { error ->
                Spacer(Modifier.height(16.dp))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditLogroBodyPreview() {
    val state = EditLogroUiState(
        NombreLogro = "Primera Victoria",
        Descripcion = "Ganar una partida en tic-tac-toe",
        EsDesbloqueado = true,
        isNew = false
    )
    MaterialTheme {
        EditLogroBody(state = state) { _ -> }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditLogroBodyNoLogradoPreview() {
    val state = EditLogroUiState(
        NombreLogro = "Victorias Consecutivas",
        Descripcion = "Ganar 5 partidas seguidas sin perder",
        EsDesbloqueado = false,
        isNew = true
    )
    MaterialTheme {
        EditLogroBody(state = state) { _ -> }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditLogroBodyConErroresPreview() {
    val state = EditLogroUiState(
        NombreLogro = "",
        Descripcion = "",
        nombreError = "Nombre requerido",
        descripcionError = "Descripción requerida",
        EsDesbloqueado = false
    )
    MaterialTheme {
        EditLogroBody(state = state) { _ -> }
    }
}