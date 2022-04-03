package com.example.githubuserdb.api

import com.example.githubuserdb.BuildConfig
import com.example.githubuserdb.data.User
import com.example.githubuserdb.data.UserDetail
import com.example.githubuserdb.data.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.ArrayList

interface ApiService {
    @Headers("Authorization: ${BuildConfig.KEY_API}")
    @GET("search/users")
    fun getUser(
        @Query("q") query : String
    ) : Call<UserResponse>

    @Headers("Authorization: ${BuildConfig.KEY_API}")
    @GET("users/{username}")
    fun getDetail(
        @Path("username") username : String
    ) : Call<UserDetail>

    @Headers("Authorization: ${BuildConfig.KEY_API}")
    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username : String
    ) : Call<ArrayList<User>>

    @Headers("Authorization: ${BuildConfig.KEY_API}")
    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username : String
    ) : Call<ArrayList<User>>
}