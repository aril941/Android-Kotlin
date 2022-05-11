package com.example.storymapapp.preferences

import android.content.Context
import com.example.storymapapp.model.UserModel

class UserPreference(context: Context) {
    private val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val userPref = pref.edit()

    fun setUser(name: String, userId: String, token: String) {
        userPref.putString(NAME, name)
        userPref.putString(TOKEN, token)
        userPref.putString(ID, userId)
        userPref.apply()
    }

    fun getUser(): UserModel {
        val userModel = UserModel()
        userModel.name = pref.getString(NAME, "").toString()
        userModel.token = pref.getString(TOKEN, "").toString()
        userModel.userId = pref.getString(ID, "").toString()
        return userModel
    }

    fun deleteUser() {
        userPref.clear()
        userPref.apply()
    }

    companion object {
        const val PREF_NAME = "user_pref"
        const val NAME = "name"
        const val TOKEN = "token"
        const val ID = "id"
    }
}