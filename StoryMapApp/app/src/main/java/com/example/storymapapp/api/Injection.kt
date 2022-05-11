package com.example.storymapapp.api

import android.content.Context
import com.example.storymapapp.preferences.UserPreference
import com.example.storymapapp.repository.StoryRepository

object Injection {
    fun InjectRepo(context: Context): StoryRepository {
        val apiService = ApiConfig.getApi()
        val token = UserPreference(context).getUser().token.toString()
        return StoryRepository(apiService, token)
    }
}