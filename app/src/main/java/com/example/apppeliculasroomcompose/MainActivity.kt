package com.example.apppeliculasroomcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.apppeliculasroomcompose.data.PeliculaDatabase
import com.example.apppeliculasroomcompose.presentacion.PantallaEditarPelicula
import com.example.apppeliculasroomcompose.presentacion.PantallaPeliculas
import com.example.apppeliculasroomcompose.ui.theme.AppPeliculasRoomComposeTheme
import com.example.apppeliculasroomcompose.viewmodel.PeliculaViewModel
import com.example.apppeliculasroomcompose.viewmodel.PeliculaViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppPeliculasRoomComposeTheme {
                val database = PeliculaDatabase.obtenerInstancia(applicationContext)
                val factory = PeliculaViewModelFactory(database.peliculaDao())
                val peliculaViewModel: PeliculaViewModel = viewModel(factory = factory)
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "inicio",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("inicio") {
                            PantallaPeliculas(
                                navController = navController,
                                viewModel = peliculaViewModel
                            )
                        }
                        composable(
                            route = "editar/{id}",
                            arguments = listOf(
                                navArgument("id") {
                                    type = NavType.IntType
                                }
                            )
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getInt("id") ?: 0
                            PantallaEditarPelicula(
                                id = id,
                                navController = navController,
                                viewModel = peliculaViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
