package com.example.githubuserapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var Image:Int,
    var name:String,
    var username:String,
    var job:String,
    var address:String,
    var repo:String,
    var follower:String,
    var following : String,
) :Parcelable
