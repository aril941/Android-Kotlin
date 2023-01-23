package com.example.project.core.data.source.local.room

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.project.core.data.source.local.entity.FilmEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface FilmDao {

    @RawQuery(observedEntities = [FilmEntity::class])
    fun getFilm(query: SupportSQLiteQuery): Flow<List<FilmEntity>>

    @Query("SELECT * FROM FilmEntities WHERE filmSearch = 0 AND title LIKE '%' || :search || '%'")
    fun getSearchFilm(search: String): Flow<List<FilmEntity>>

    @RawQuery(observedEntities = [FilmEntity::class])
    fun getFavoriteFilm(query: SupportSQLiteQuery): Flow<List<FilmEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilm(movies: List<FilmEntity>)

    @Update
    fun updateFavoriteFilm(movie: FilmEntity)
}