package com.example.registrojugadoresjohanreinosoap2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.registrojugadoresjohanreinosoap2.presentation.edit.EditPlayerScreen
import com.example.registrojugadoresjohanreinosoap2.presentation.gamePresentation.edit.EditGameScreen
import com.example.registrojugadoresjohanreinosoap2.presentation.gamePresentation.list.GameListScreen
import com.example.registrojugadoresjohanreinosoap2.presentation.list.ListPlayerViewModel
import com.example.registrojugadoresjohanreinosoap2.presentation.list.PlayerListScreen
import com.example.registrojugadoresjohanreinosoap2.ui.theme.RegistroJugadoresJohanReinosoAp2Theme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistroJugadoresJohanReinosoAp2Theme {
                val navController = rememberNavController()
                val listPlayerViewModel: ListPlayerViewModel = hiltViewModel()
                val playerState by listPlayerViewModel.state.collectAsStateWithLifecycle()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {
                            Text("Menú", modifier = Modifier.padding(16.dp))
                            Divider()
                            NavigationDrawerItem(
                                label = { Text(text = "Registro de Jugadores") },
                                selected = false,
                                onClick = {
                                    navController.navigate("playerList")
                                    scope.launch { drawerState.close() }
                                }
                            )
                            NavigationDrawerItem(
                                label = { Text(text = "Registro de Partidas") },
                                selected = false,
                                onClick = {
                                    navController.navigate("gameList")
                                    scope.launch { drawerState.close() }
                                }
                            )
                        }
                    }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        IconButton(
                            onClick = {
                                scope.launch { drawerState.open() }
                            },
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(16.dp)
                        ) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menú")
                        }

                        NavHost(
                            navController = navController,
                            startDestination = "playerList",
                            modifier = Modifier.fillMaxSize()
                        ) {
                            composable("playerList") {
                                PlayerListScreen(
                                    onNavigateToEdit = { id ->
                                        navController.navigate("editPlayer/$id")
                                    },
                                    onNavigateToCreate = {
                                        navController.navigate("editPlayer/0")
                                    },
                                    onOpenDrawer = {
                                        scope.launch { drawerState.open() }
                                    }
                                )
                            }
                            composable("editPlayer/{id}") { backStackEntry ->
                                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                                EditPlayerScreen(playerId = id,
                                    navController = navController,
                                    viewModel = hiltViewModel())
                            }
                            composable("gameList") {
                                GameListScreen(
                                    onNavigateGameEdit = { id ->
                                        navController.navigate("editGame/$id")
                                    },
                                    onNavigateGameCreate = {
                                        navController.navigate("editGame/0")
                                    },
                                    onOpenDrawer = {
                                        scope.launch { drawerState.open() }
                                    }
                                )
                            }
                            composable("editGame/{id}") { backStackEntry ->
                                val id = backStackEntry.arguments?.getString("id")
                                EditGameScreen(
                                    gameId = id?.toIntOrNull(),
                                    navController = navController,
                                    viewModel = hiltViewModel()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}