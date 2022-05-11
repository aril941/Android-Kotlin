package com.example.storyapp.api

import com.example.storyapp.data.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("/v1/register")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email : String,
        @Field("password") password : String
    ): Call<UserStoryResponse>

    @POST("/v1/login")
    @FormUrlEncoded
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>


    @GET("/v1/stories")
    fun getAllStories(
        @Header("Authorization")token: String
    ): Call<AllStoryResponse>

    @Multipart
    @POST("/v1/stories")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String
    ): Call<AddStoryResponse>
}