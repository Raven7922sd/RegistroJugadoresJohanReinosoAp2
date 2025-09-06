package com.example.registrojugadoresjohanreinosoap2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.registrojugadoresjohanreinosoap2.ui.theme.RegistroJugadoresJohanReinosoAp2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistroJugadoresJohanReinosoAp2Theme {

            val navHost= rememberNavController()
                RegistroJugadores(navHost)
            }
        }
    }
}