package com.example.storymapapp.data

import com.google.gson.annotations.SerializedName

data class StoryMap(
    @field:SerializedName("listStory")
    val listStory: ArrayList<ListStoryItem>,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)