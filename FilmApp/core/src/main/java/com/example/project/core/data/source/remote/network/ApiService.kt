package com.example.project.core.data.source.remote.network

import com.example.project.core.data.source.remote.response.ListFilmResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("movie")
    suspend fun getFilm(
        @Query("api_key") apiKey: String,
    ): ListFilmResponse
}