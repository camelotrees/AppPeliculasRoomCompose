package com.example.apppeliculasroomcompose.presentacion

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.apppeliculasroomcompose.data.Pelicula
import com.example.apppeliculasroomcompose.viewmodel.PeliculaViewModel

@Composable
fun PantallaPeliculas(
    navController: NavHostController,
    viewModel: PeliculaViewModel
) {
    val context = LocalContext.current
    var titulo by rememberSaveable { mutableStateOf("") }
    var genero by rememberSaveable { mutableStateOf("") }
    var anioTexto by rememberSaveable { mutableStateOf("") }
    var peliculaAEliminar by remember { mutableStateOf<Pelicula?>(null) }

    val peliculas by viewModel.listaPeliculas.collectAsStateWithLifecycle(initialValue = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Registro de películas",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = genero,
            onValueChange = { genero = it },
            label = { Text("Género") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = anioTexto,
            onValueChange = { anioTexto = it },
            label = { Text("Año") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val anio = anioTexto.trim().toIntOrNull()
                if (titulo.isBlank() || genero.isBlank() || anioTexto.isBlank()) {
                    Toast.makeText(context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                } else if (anio == null || (anio !in 1900..2100)) {
                    Toast.makeText(context, "El año debe ser un número entre 1900 y 2100", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.agregarPelicula(titulo.trim(), genero.trim(), anio)
                    titulo = ""
                    genero = ""
                    anioTexto = ""
                    Toast.makeText(context, "Película agregada con éxito", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar Película")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Lista de Películas",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(peliculas, key = { it.id }) { pelicula ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = pelicula.titulo,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Género: ${pelicula.genero}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Año: ${pelicula.anio}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "ID: ${pelicula.id}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }

                        Row {
                            IconButton(
                                onClick = {
                                    navController.navigate("editar/${pelicula.id}")
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = "Editar película"
                                )
                            }
                            IconButton(
                                onClick = {
                                    peliculaAEliminar = pelicula
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Eliminar película"
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (peliculaAEliminar != null) {
        AlertDialog(
            onDismissRequest = { peliculaAEliminar = null },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Desea eliminar la película \"${peliculaAEliminar?.titulo}\"?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        peliculaAEliminar?.let { pelicula ->
                            viewModel.eliminarPelicula(pelicula)
                            Toast.makeText(context, "Película eliminada", Toast.LENGTH_SHORT).show()
                        }
                        peliculaAEliminar = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { peliculaAEliminar = null }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
