package com.example.apppeliculasroomcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apppeliculasroomcompose.data.Pelicula
import com.example.apppeliculasroomcompose.data.PeliculaDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PeliculaViewModel(private val dao: PeliculaDao) : ViewModel() {
    val listaPeliculas: Flow<List<Pelicula>> = dao.obtenerTodas()

    fun agregarPelicula(titulo: String, genero: String, anio: Int) {
        viewModelScope.launch {
            dao.insertar(Pelicula(titulo = titulo, genero = genero, anio = anio))
        }
    }

    fun actualizarPelicula(id: Int, titulo: String, genero: String, anio: Int) {
        viewModelScope.launch {
            dao.actualizar(
                Pelicula(id = id, titulo = titulo, genero = genero, anio = anio)
            )
        }
    }

    fun eliminarPelicula(pelicula: Pelicula) {
        viewModelScope.launch {
            dao.eliminar(pelicula)
        }
    }

    suspend fun obtenerPelicula(id: Int): Pelicula? {
        return dao.obtenerPeliculaPorId(id)
    }

    suspend fun buscarPorAnio(anio: Int): List<Pelicula> {
        return dao.buscarPorAnio(anio)
    }
}

