package com.example.registrojugadoresjohanreinosoap2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.registrojugadoresjohanreinosoap2.presentation.Players.edit.EditPlayerScreen
import com.example.registrojugadoresjohanreinosoap2.presentation.gamePresentation.edit.EditGameScreen
import com.example.registrojugadoresjohanreinosoap2.presentation.gamePresentation.list.GameListScreen
import com.example.registrojugadoresjohanreinosoap2.presentation.Players.list.ListPlayerViewModel
import com.example.registrojugadoresjohanreinosoap2.presentation.Players.list.PlayerListScreen
import com.example.registrojugadoresjohanreinosoap2.presentation.gamePresentation.GameScreen
import com.example.registrojugadoresjohanreinosoap2.ui.theme.RegistroJugadoresJohanReinosoAp2Theme
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.registrojugadores.Presentation.Logros.Edit.EditLogroScreen
import edu.ucne.registrojugadores.Presentation.Logros.List.ListLogroScreen
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
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
                        ModalDrawerSheet(
                            modifier = Modifier.width(280.dp)
                        ) {
                            DrawerHeader()
                            LazyColumn {
                                item {
                                    DrawerMenuItem(
                                        icon = Icons.Filled.PlayArrow,
                                        title = "Tic-Tac-Toe",
                                        isSelected = navController.currentDestination?.route == "tictactoeGame",
                                        onClick = {
                                            navController.navigate("tictactoeGame")
                                            scope.launch { drawerState.close() }
                                        }
                                    )
                                }
                                item {
                                    DrawerMenuItem(
                                        icon = Icons.Filled.Person,
                                        title = "Registro de Jugadores",
                                        isSelected = navController.currentDestination?.route == "playerList",
                                        onClick = {
                                            navController.navigate("playerList")
                                            scope.launch { drawerState.close() }
                                        }
                                    )
                                }
                                item {
                                    DrawerMenuItem(
                                        icon = Icons.Filled.PlayArrow,
                                        title = "Registro de Partidas",
                                        isSelected = navController.currentDestination?.route == "gameList",
                                        onClick = {
                                            navController.navigate("gameList")
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
                            }
                        }
                    }
                ) {
                    Scaffold(
                        topBar = {
                            CustomTopAppBar(
                                onMenuClick = {
                                    scope.launch { drawerState.open() }
                                }
                            )
                        },
                        containerColor = Color(0xFFF8F9FA)
                    ) { paddingValues ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
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
                                startDestination = "tictactoeGame",
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
                                    val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                                    EditPlayerScreen(
                                        playerId = id,
                                        navController = navController,
                                        viewModel = hiltViewModel()
                                    )
                                }

                                composable("gameList") {
                                    GameListScreen(
                                        onNavigateToEdit = { id ->
                                            navController.navigate("editGame/$id")
                                        },
                                        onNavigateToCreate = {
                                            navController.navigate("editGame/0")
                                        },
                                        onNavigateToPlayers = {
                                            navController.navigate("playerList")
                                        },
                                        onNavigateToGame = {
                                            navController.navigate("gameScreen")
                                        },
                                        onContinueGame = { GameId ->
                                            navController.navigate("gameScreen/$GameId")
                                        },
                                        onOpenDrawer = {
                                            scope.launch { drawerState.open() }
                                        })
                                }

                                composable("tictactoeGame") {
                                    GameScreen(
                                        viewModel = hiltViewModel()
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


                                composable("gameScreen/{partidaId}") { backStackEntry ->
                                    val partidaId = backStackEntry.arguments?.getString("partidaId")?.toIntOrNull()
                                    GameScreen(
                                        partidaId = partidaId
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

                                composable("editLogro/{logroId}") { backStackEntry ->
                                    val logroId = backStackEntry.arguments?.getString("logroId")?.toIntOrNull()
                                    EditLogroScreen(
                                        logroId = logroId,
                                        onSaveComplete = { navController.popBackStack() },
                                        onDeleteComplete = { navController.popBackStack() }
                                    )
                                }
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
            .height(120.dp)
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
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Menú Principal",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Gestión de Juegos",
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodySmall
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
        shape = RoundedCornerShape(12.dp),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomTopAppBar(
    onMenuClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Tic-Tac-Toe App",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        },
        navigationIcon = {
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.1f))
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menú",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF7E57C2)
        ),
        modifier = Modifier.shadow(
            elevation = 8.dp,
            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
        )
    )
}