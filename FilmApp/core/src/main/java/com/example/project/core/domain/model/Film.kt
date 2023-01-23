package com.example.project.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Film(
    var overview: String,
    var releaseDate: String,
    var popularity: Double,
    var id: Int,
    var title: String,
    var posterPath: String,
    var favorite: Boolean = false,
) : Parcelable