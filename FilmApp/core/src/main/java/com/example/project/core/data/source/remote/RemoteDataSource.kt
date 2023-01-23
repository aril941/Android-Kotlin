package com.example.project.core.data.source.remote

import com.example.project.core.data.source.remote.network.ApiResponse
import com.example.project.core.data.source.remote.network.ApiService
import com.example.project.core.data.source.remote.response.FilmResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiService: ApiService) {

    private val Key = "a95637cef4be9ff29adfe52f7a5c84ac"
    suspend fun getFilm(): Flow<ApiResponse<List<FilmResponse>>> {
        return flow {
            try {
                val response = apiService.getFilm(Key)
                val movieList = response.results
                if (movieList.isNotEmpty()) {
                    emit(ApiResponse.Success(response.results))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
}
