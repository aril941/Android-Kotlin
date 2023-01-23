package com.example.project.core.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FilmEntities")
data class FilmEntity(

    @ColumnInfo(name = "overview")
    var overview: String,

    @ColumnInfo(name = "releaseDate")
    var releaseDate: String,

    @ColumnInfo(name = "popularity")
    var popularity: Double,

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "posterPath")
    var posterPath: String,

    @ColumnInfo(name = "favorite")
    var favorite: Boolean = false,

    @ColumnInfo(name = "filmSearch")
    var filmSearch: Boolean = false
)
