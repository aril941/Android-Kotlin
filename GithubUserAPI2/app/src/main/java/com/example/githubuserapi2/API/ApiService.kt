package com.example.githubuserapi2.API

import com.example.githubuserapi2.Data.User
import com.example.githubuserapi2.Data.UserDetail
import com.example.githubuserapi2.Data.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.ArrayList

interface ApiService {

    @Headers("Authorization: token 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")
    @GET("search/users")
    fun getUser(
        @Query("q") query : String
    ) : Call<UserResponse>

    @Headers("Authorization: token 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")
    @GET("users/{username}")
    fun getDetail(
        @Path("username") username : String
    ) : Call<UserDetail>

    @Headers("Authorization: token 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")
    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username : String
    ) : Call<ArrayList<User>>

    @Headers("Authorization: token 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")
    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username : String
    ) : Call<ArrayList<User>>
}