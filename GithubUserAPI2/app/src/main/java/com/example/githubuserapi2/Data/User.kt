package com.example.githubuserapi2.Data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val login: String,
    val id : Int,
    @field:SerializedName("avatar_url")
    val avatarUrl: String,
) : Parcelable