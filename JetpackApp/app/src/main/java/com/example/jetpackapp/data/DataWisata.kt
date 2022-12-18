package com.example.jetpackapp.data

import java.io.Serializable

data class DataWisata(
    val id: Long,
    val name: String,
    val photo: Int,
    val desc: String
):Serializable
