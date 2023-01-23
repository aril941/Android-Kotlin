package com.example.project.filmapp.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.project.core.domain.usecase.FilmAppUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*

@FlowPreview
@ExperimentalCoroutinesApi
class SearchViewModel(private val filmAppUseCase: FilmAppUseCase) : ViewModel() {
    private val querySearch = ConflatedBroadcastChannel<String>()

    fun setSearch(search: String) {
        querySearch.offer(search)
    }
    val movieResult = querySearch.asFlow()
        .debounce(300)
        .distinctUntilChanged()
        .filter {
            it.trim().isNotEmpty()
        }
        .flatMapLatest {
            filmAppUseCase.getSearchFilm(it)
        }.asLiveData()
}
