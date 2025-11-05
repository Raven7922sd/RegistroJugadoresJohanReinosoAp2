package com.example.registrojugadoresjohanreinosoap2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.registrojugadoresjohanreinosoap2.presentation.Players.edit.EditPlayerScreen
import com.example.registrojugadoresjohanreinosoap2.presentation.gamePresentation.edit.EditGameScreen
import com.example.registrojugadoresjohanreinosoap2.presentation.gamePresentation.list.GameListScreen
import com.example.registrojugadoresjohanreinosoap2.presentation.Players.list.ListPlayerViewModel
import com.example.registrojugadoresjohanreinosoap2.presentation.Players.list.PlayerListScreen
import com.example.registrojugadoresjohanreinosoap2.presentation.apiPresentation.ApiGameScreen
import com.example.registrojugadoresjohanreinosoap2.presentation.apiPresentation.list.ListPartidaApiScreen
import com.example.registrojugadoresjohanreinosoap2.presentation.gamePresentation.GameScreen
import com.example.registrojugadoresjohanreinosoap2.ui.theme.RegistroJugadoresJohanReinosoAp2Theme
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.registrojugadores.Presentation.Logros.Edit.EditLogroScreen
import edu.ucne.registrojugadores.Presentation.Logros.List.ListLogroScreen
import kotlinx.coroutines.launch
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.ui.text.style.TextAlign
import androidx.core.view.WindowCompat
import androidx.navigation.NavController


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

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
                        ModalDrawerSheet(
                            modifier = Modifier.width(280.dp)
                        ) {
                            DrawerHeader()
                            LazyColumn {
                                item {
                                    DrawerMenuItem(
                                        icon = Icons.Filled.Person,
                                        title = "Jugadores",
                                        isSelected = navController.currentDestination?.route == "playerList",
                                        onClick = {
                                            navController.navigate("playerList") {
                                                popUpTo("playerList") { inclusive = true }
                                            }
                                            scope.launch { drawerState.close() }
                                        }
                                    )
                                }
                                item {
                                    DrawerMenuItem(
                                        icon = Icons.Filled.PlayArrow,
                                        title = "Partidas",
                                        isSelected = navController.currentDestination?.route == "partidaList",
                                        onClick = {
                                            navController.navigate("partidaList")
                                            scope.launch { drawerState.close() }
                                        }
                                    )
                                }
                                item {
                                    DrawerMenuItem(
                                        icon = Icons.Filled.Star,
                                        title = "Logros",
                                        isSelected = navController.currentDestination?.route == "logroList",
                                        onClick = {
                                            navController.navigate("logroList")
                                            scope.launch { drawerState.close() }
                                        }
                                    )
                                }
                                item {
                                    DrawerMenuItem(
                                        icon = Icons.Filled.Wifi,
                                        title = "Partidas API",
                                        isSelected = navController.currentDestination?.route == "apiPartidaList",
                                        onClick = {
                                            navController.navigate("apiPartidaList")
                                            scope.launch { drawerState.close() }
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFFF8F9FA),
                                        Color(0xFFE9ECEF)
                                    )
                                )
                            )
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = "playerList",
                            modifier = Modifier.fillMaxSize(),
                            enterTransition = { slideInHorizontally(initialOffsetX = { 300 }) },
                            exitTransition = { slideOutHorizontally(targetOffsetX = { -300 }) }
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
                                val id = backStackEntry.arguments?.getString("id")
                                EditPlayerScreen(playerId = id)
                            }

                            composable("partidaList") {
                                GameListScreen(
                                    onNavigateToEdit = { id ->
                                        navController.navigate("editPartida/$id")
                                    },
                                    onNavigateToCreate = {
                                        navController.navigate("editPartida/0")
                                    },
                                    onNavigateToPlayers = {
                                        navController.navigate("playerList")
                                    },
                                    onNavigateToGame = {
                                        navController.navigate("gameScreen")
                                    },
                                    onContinueGame = { partidaId ->
                                        navController.navigate("gameScreen/$partidaId")
                                    }
                                )
                            }

                            composable(
                                route = "editPartida/{id}",
                                arguments = listOf(navArgument("id") { type = NavType.IntType; defaultValue = 0 })
                            ) { backStackEntry ->
                                val id = backStackEntry.arguments?.getInt("id")
                                EditGameScreen(
                                    gameId = id,
                                    navController = navController,
                                    viewModel = hiltViewModel()
                                )
                            }

                            composable(
                                route = "gameScreen",
                            ) {
                                GameScreen(
                                    partidaId = null,
                                    viewModel = hiltViewModel()
                                )
                            }

                            composable(
                                route = "gameScreen/{partidaId}",
                                arguments = listOf(navArgument("partidaId") { type = NavType.IntType })
                            ) { backStackEntry ->
                                val partidaId = backStackEntry.arguments?.getInt("partidaId")
                                GameScreen(
                                    partidaId = partidaId,
                                    viewModel = hiltViewModel()
                                )
                            }

                            composable("logroList") {
                                ListLogroScreen(
                                    onNavigateToEdit = { logroId ->
                                        navController.navigate("editLogro/$logroId")
                                    },
                                    onNavigateToCreate = {
                                        navController.navigate("editLogro/0")
                                    }
                                )
                            }

                            composable(
                                route = "editLogro/{logroId}",
                                arguments = listOf(navArgument("logroId") { type = NavType.IntType; defaultValue = 0 })
                            ) { backStackEntry ->
                                val logroId = backStackEntry.arguments?.getInt("logroId")
                                EditLogroScreen(
                                    logroId = logroId,
                                    onSaveComplete = { navController.popBackStack() },
                                    onDeleteComplete = { navController.popBackStack() }
                                )
                            }

                            composable("apiPartidaList") {
                                ListPartidaApiScreen(
                                    onNavigateToCreate = {
                                        navController.navigate("apiGameScreen")
                                    },
                                    onNavigateToGame = { partidaId ->
                                        navController.navigate("apiGameScreen/$partidaId")
                                    }
                                )
                            }

                            composable("apiGameScreen") {
                                ApiGameScreen(
                                    partidaId = null,
                                    onNavigateBack = { navController.popBackStack() }
                                )
                            }

                            composable(
                                route = "apiGameScreen/{partidaId}",
                                arguments = listOf(navArgument("partidaId") { type = NavType.IntType })
                            ) { backStackEntry ->
                                val partidaId = backStackEntry.arguments?.getInt("partidaId")
                                ApiGameScreen(
                                    partidaId = partidaId,
                                    onNavigateBack = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF6A1B9A),
                        Color(0xFF7E57C2)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.SportsEsports,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Menú Principal",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Gestión de opciones",
                color = Color.White.copy(alpha = 0.9f),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun DrawerMenuItem(
    icon: ImageVector,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color(0xFF7E57C2) else Color.Gray
            )
        },
        label = {
            Text(
                text = title,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) Color(0xFF7E57C2) else Color.Black
            )
        },
        selected = isSelected,
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = Color(0xFF7E57C2).copy(alpha = 0.1f),
            unselectedContainerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp)
    )
}