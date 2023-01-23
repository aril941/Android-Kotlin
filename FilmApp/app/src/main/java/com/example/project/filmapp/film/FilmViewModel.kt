package com.example.project.filmapp.film

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.project.core.data.Resource
import com.example.project.core.domain.model.Film
import com.example.project.core.domain.usecase.FilmAppUseCase

class FilmViewModel(private val filmAppUseCase: FilmAppUseCase) : ViewModel() {
    fun getFilm(sort: String): LiveData<Resource<List<Film>>> =
        filmAppUseCase.getAllFilm(sort).asLiveData()
}