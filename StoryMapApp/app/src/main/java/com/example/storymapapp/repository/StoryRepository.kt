package com.example.storymapapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storymapapp.api.ApiService
import com.example.storymapapp.data.ListStoryItem
import com.example.storymapapp.paging.StoryPagingSource

class StoryRepository(private val apiService: ApiService, private val token: String) {
    fun getStoryRepo(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, token)
            }
        ).liveData
    }
}