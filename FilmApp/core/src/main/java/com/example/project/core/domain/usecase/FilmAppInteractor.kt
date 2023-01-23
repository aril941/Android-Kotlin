package com.example.project.core.domain.usecase

import com.example.project.core.data.Resource
import com.example.project.core.domain.model.Film
import com.example.project.core.domain.repository.DomainFilmAppRepository
import kotlinx.coroutines.flow.Flow

class FilmAppInteractor(private val domainFilmAppRepository: DomainFilmAppRepository) : FilmAppUseCase {

    override fun getAllFilm(sort: String): Flow<Resource<List<Film>>> =
        domainFilmAppRepository.getAllFilm(sort)

    override fun getFavoriteFilm(sort: String): Flow<List<Film>> =
        domainFilmAppRepository.getFavoriteFilm(sort)

    override fun getSearchFilm(search: String): Flow<List<Film>> =
        domainFilmAppRepository.getSearchFilm(search)

    override fun setFilmFavorite(film: Film, state: Boolean) =
        domainFilmAppRepository.setFilmFavorite(film, state)

}