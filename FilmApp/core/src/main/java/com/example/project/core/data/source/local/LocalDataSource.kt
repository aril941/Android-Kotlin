package com.example.project.core.data.source.local

import com.example.project.core.data.source.local.entity.FilmEntity
import com.example.project.core.data.source.local.room.FilmDao
import com.example.project.core.utils.SortUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn

class LocalDataSource(private val mFilmDao: FilmDao) {

    fun getAllFilm(sort: String): Flow<List<FilmEntity>> {
        val query = SortUtils.getSortedQueryFilm(sort)
        return mFilmDao.getFilm(query)
    }

    fun getAllFavoriteFilm(sort: String): Flow<List<FilmEntity>> {
        val query = SortUtils.getSortedQueryFavoriteFilm(sort)
        return mFilmDao.getFavoriteFilm(query)
    }

    fun getFilmSearch(search: String): Flow<List<FilmEntity>> {
        return mFilmDao.getSearchFilm(search)
            .flowOn(Dispatchers.Default)
            .conflate()
    }

    suspend fun insertFilm(Film: List<FilmEntity>) = mFilmDao.insertFilm(Film)

    fun setFilmFavorite(film: FilmEntity, newState: Boolean) {
        film.favorite = newState
        mFilmDao.updateFavoriteFilm(film)
    }
}