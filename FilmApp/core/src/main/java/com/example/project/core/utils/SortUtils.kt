package com.example.project.core.utils

import androidx.sqlite.db.SimpleSQLiteQuery

object SortUtils {

    const val POPULARITY = "Popularity"
    const val VOTE = "Vote"
    const val NEWEST = "Newest"
    const val RANDOM = "Random"

    fun getSortedQueryFilm(filter: String): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM FilmEntities where filmSearch = 0 ")
        when (filter) {
            POPULARITY -> {
                simpleQuery.append("ORDER BY popularity DESC")
            }
            NEWEST -> {
                simpleQuery.append("ORDER BY releaseDate DESC")
            }
            VOTE -> {
                simpleQuery.append("ORDER BY voteAverage DESC")
            }
            RANDOM -> {
                simpleQuery.append("ORDER BY RANDOM()")
            }
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }

    fun getSortedQueryFavoriteFilm(filter: String): SimpleSQLiteQuery {
        val simpleQuery =
            StringBuilder().append("SELECT * FROM FilmEntities where favorite = 1 and filmSearch = 0 ")
        when (filter) {
            POPULARITY -> {
                simpleQuery.append("ORDER BY popularity DESC")
            }
            NEWEST -> {
                simpleQuery.append("ORDER BY releaseDate DESC")
            }
            VOTE -> {
                simpleQuery.append("ORDER BY voteAverage DESC")
            }
            RANDOM -> {
                simpleQuery.append("ORDER BY RANDOM()")
            }
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }
}