package com.example.storymapapp.view

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storymapapp.api.Injection
import com.example.storymapapp.data.ListStoryItem
import com.example.storymapapp.repository.StoryRepository

class StoryPagingViewModel(storiesRepository: StoryRepository) : ViewModel() {
    val story: LiveData<PagingData<ListStoryItem>> =
        storiesRepository.getStoryRepo().cachedIn(viewModelScope)
}

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryPagingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryPagingViewModel(Injection.InjectRepo(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}