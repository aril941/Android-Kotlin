package com.example.project.core.domain.repository

import com.example.project.core.data.Resource
import com.example.project.core.domain.model.Film
import kotlinx.coroutines.flow.Flow

interface DomainFilmAppRepository {

    fun getAllFilm(sort: String): Flow<Resource<List<Film>>>

    fun getFavoriteFilm(sort: String): Flow<List<Film>>

    fun getSearchFilm(search: String): Flow<List<Film>>

    fun setFilmFavorite(film: Film, state: Boolean)

}