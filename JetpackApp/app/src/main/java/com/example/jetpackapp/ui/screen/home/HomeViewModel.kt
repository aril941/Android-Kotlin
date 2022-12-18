package com.example.jetpackapp.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackapp.data.OrderDataWisata
import com.example.jetpackapp.repository.WisataRepository
import com.example.jetpackapp.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    val repository: WisataRepository) : ViewModel() {
    val _uiState: MutableStateFlow<UiState<List<OrderDataWisata>>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<OrderDataWisata>>>
        get() = _uiState


    fun getHomeDetail() {
        viewModelScope.launch {
            repository.getHomeDetailRepo()
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { wisataRepo ->
                    _uiState.value = UiState.Success(wisataRepo)
                }
        }
    }
}