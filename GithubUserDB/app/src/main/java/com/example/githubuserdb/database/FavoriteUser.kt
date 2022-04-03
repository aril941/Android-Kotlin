package com.example.githubuserdb.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorite")
data class FavoriteUser(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = 0,
    @ColumnInfo(name = "username")
    var login: String?,
    @ColumnInfo(name = "photo")
    var avatarUrl: String?,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "location")
    val location: String?,
    @ColumnInfo(name = "followers")
    val followers: String?,
    @ColumnInfo(name = "company")
    val company: String?,
    @ColumnInfo(name = "following")
    val following: String?,
    @ColumnInfo(name = "public_repos")
    val publicRepo: String?


): Serializable
