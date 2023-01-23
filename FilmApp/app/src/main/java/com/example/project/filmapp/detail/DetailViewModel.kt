package com.example.project.filmapp.detail

import androidx.lifecycle.ViewModel
import com.example.project.core.domain.model.Film
import com.example.project.core.domain.usecase.FilmAppUseCase

class DetailViewModel(private val filmAppUseCase: FilmAppUseCase) : ViewModel() {

    fun setFavoriteMovie(film: Film, newStatus: Boolean) {
        filmAppUseCase.setFilmFavorite(film, newStatus)
    }
}
