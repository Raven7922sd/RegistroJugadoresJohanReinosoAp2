package com.example.registrojugadoresjohanreinosoap2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.registrojugadoresjohanreinosoap2.presentation.edit.EditPlayerScreen
import com.example.registrojugadoresjohanreinosoap2.presentation.list.PlayerListScreen
import com.example.registrojugadoresjohanreinosoap2.ui.theme.RegistroJugadoresJohanReinosoAp2Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistroJugadoresJohanReinosoAp2Theme {

                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "playerList"
                ) {
                    composable("playerList") {
                        PlayerListScreen(
                            onNavigateToEdit = { id ->
                                navController.navigate("editPlayer/$id")
                            },
                            onNavigateToCreate = {
                                navController.navigate("editPlayer/0")
                            }
                        )
                }
                    composable("editPlayer/{id}") { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                        EditPlayerScreen(playerId = id, navController = navController)
            }
        }
    }
}
    }
}