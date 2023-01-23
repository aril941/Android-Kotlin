package com.example.project.core.utils

import com.example.project.core.data.source.local.entity.FilmEntity
import com.example.project.core.data.source.remote.response.FilmResponse

import com.example.project.core.domain.model.Film

object DataMapper {
    fun mapFilmResponsesToEntities(input: List<FilmResponse>): List<FilmEntity> {
        val movieList = ArrayList<FilmEntity>()
        input.map {
            val movie = FilmEntity(
                it.overview,
                it.releaseDate,
                it.popularity,
                it.id,
                it.title,
                it.posterPath,
                favorite = false,
            )
            movieList.add(movie)
        }
        return movieList
    }

    fun mapEntitiesToDomain(input: List<FilmEntity>): List<Film> {
        return input.map {
            Film(
                it.overview,
                it.releaseDate,
                it.popularity,
                it.id,
                it.title,
                it.posterPath,
                favorite = it.favorite,
            )
        }
    }

    fun mapDomainToEntity(input: Film): FilmEntity {
        return FilmEntity(
            input.overview,
            input.releaseDate,
            input.popularity,
            input.id,
            input.title,
            input.posterPath,
            favorite = input.favorite,
        )
    }
}