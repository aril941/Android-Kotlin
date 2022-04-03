package com.example.githubuserdb.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val login: String,
    val id: Int?,
    @field:SerializedName("avatar_url")
    val avatarUrl: String,
    @field:SerializedName("location")
    val location: String,
    @field:SerializedName("followers")
    val followers: String,
    @field:SerializedName("company")
    val company: String,
    @field:SerializedName("following")
    val following: String,
    @field:SerializedName("public_repos")
    val publicRepo: String
) : Parcelable