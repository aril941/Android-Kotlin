package com.example.project.favorite.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.project.core.domain.model.Film
import com.example.project.core.domain.usecase.FilmAppUseCase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

class FavoriteViewModel(private val filmAppUseCase: FilmAppUseCase) : ViewModel() {

    fun getFavoriteFilm(sort: String): LiveData<List<Film>> =
        filmAppUseCase.getFavoriteFilm(sort).asLiveData()

    fun setFavorite(Film: Film, newState: Boolean) {
        filmAppUseCase.setFilmFavorite(Film, newState)
    }
}

val favoriteModule = module {
    viewModel {
        FavoriteViewModel(get())
    }
}