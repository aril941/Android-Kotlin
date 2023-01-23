package com.example.project.core.data

import com.example.project.core.data.source.local.LocalDataSource
import com.example.project.core.data.source.remote.RemoteDataSource
import com.example.project.core.data.source.remote.network.ApiResponse
import com.example.project.core.data.source.remote.response.FilmResponse
import com.example.project.core.domain.model.Film
import com.example.project.core.domain.repository.DomainFilmAppRepository
import com.example.project.core.utils.AppExecutors
import com.example.project.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FilmAppRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : DomainFilmAppRepository {

    override fun getAllFilm(sort: String): Flow<Resource<List<Film>>> =
        object : NetworkBoundResource<List<Film>, List<FilmResponse>>() {
            override fun loadFromDB(): Flow<List<Film>> {
                return localDataSource.getAllFilm(sort).map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Film>?): Boolean {
                return data == null || data.isEmpty()
            }

            override suspend fun createCall(): Flow<ApiResponse<List<FilmResponse>>> {
                return remoteDataSource.getFilm()
            }

            override suspend fun saveCallResult(data: List<FilmResponse>) {
                val filmList = DataMapper.mapFilmResponsesToEntities(data)
                localDataSource.insertFilm(filmList)
            }
        }.asFlow()


    override fun getSearchFilm(search: String): Flow<List<Film>> {
        return localDataSource.getFilmSearch(search).map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }


    override fun getFavoriteFilm(sort: String): Flow<List<Film>> {
        return localDataSource.getAllFavoriteFilm(sort).map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override fun setFilmFavorite(film: Film, state: Boolean) {
        val filmEntity = DataMapper.mapDomainToEntity(film)
        appExecutors.diskIO().execute { localDataSource.setFilmFavorite(filmEntity, state) }
    }
}