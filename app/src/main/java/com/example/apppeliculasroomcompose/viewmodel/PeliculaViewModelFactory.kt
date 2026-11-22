package com.example.apppeliculasroomcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.apppeliculasroomcompose.data.PeliculaDao

class PeliculaViewModelFactory(
    private val dao: PeliculaDao
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PeliculaViewModel::class.java)) {
            return PeliculaViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}