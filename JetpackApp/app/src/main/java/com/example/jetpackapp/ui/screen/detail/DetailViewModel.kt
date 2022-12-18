package com.example.jetpackapp.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackapp.data.OrderDataWisata
import com.example.jetpackapp.repository.WisataRepository
import com.example.jetpackapp.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailRewardViewModel(
    val repository: WisataRepository
) : ViewModel() {
    val _uiState: MutableStateFlow<UiState<OrderDataWisata>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<OrderDataWisata>>
        get() = _uiState

    fun getWisataById(detailId: Long) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = UiState.Success(repository.getOrderWisataById(detailId))
        }
    }
}