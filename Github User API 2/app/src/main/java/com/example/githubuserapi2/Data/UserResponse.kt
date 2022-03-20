package com.example.githubuserapi2.Data

import com.google.gson.annotations.SerializedName

data class UserResponse(

    @field:SerializedName("items")
    val items: ArrayList<User>
)
