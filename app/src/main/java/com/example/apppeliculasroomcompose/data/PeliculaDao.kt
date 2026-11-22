package com.example.apppeliculasroomcompose.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PeliculaDao {

    @Insert
    suspend fun insertar(pelicula: Pelicula)

    // Del registro más reciente al más antiguo
    @Query("SELECT * FROM peliculas ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<Pelicula>>

    @Update
    suspend fun actualizar(pelicula: Pelicula)

    @Delete
    suspend fun eliminar(pelicula: Pelicula)

    // Devuelve null si no existe una película con ese id
    @Query("SELECT * FROM peliculas WHERE id = :id")
    suspend fun obtenerPeliculaPorId(id: Int): Pelicula?

    // Películas estrenadas en un año, ordenadas por título
    @Query("SELECT * FROM peliculas WHERE anio = :anio ORDER BY titulo")
    suspend fun buscarPorAnio(anio: Int): List<Pelicula>
}