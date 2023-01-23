package com.example.project.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ListFilmResponse(

	@field:SerializedName("results")
	val results: List<FilmResponse>
)

data class FilmResponse(

	@field:SerializedName("overview")
	val overview: String,

	@field:SerializedName("release_date")
	val releaseDate: String,

	@field:SerializedName("popularity")
	val popularity: Double,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("poster_path")
	val posterPath: String
)
