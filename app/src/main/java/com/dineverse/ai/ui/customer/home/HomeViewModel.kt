package com.dineverse.ai.ui.customer.home

import androidx.lifecycle.viewModelScope
import com.dineverse.ai.core.base.BaseViewModel
import com.dineverse.ai.core.common.Resource
import com.dineverse.ai.domain.usecase.GetRestaurantsUseCase
import com.dineverse.ai.domain.usecase.SearchRestaurantsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRestaurantsUseCase: GetRestaurantsUseCase,
    private val searchRestaurantsUseCase: SearchRestaurantsUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(RestaurantUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getRestaurants()
    }

    fun getRestaurants() {
        getRestaurantsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _uiState.value = RestaurantUiState(restaurants = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _uiState.value = RestaurantUiState(error = result.message ?: "An unexpected error occurred")
                }
                is Resource.Loading -> {
                    _uiState.value = RestaurantUiState(isLoading = true)
                }
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    fun searchRestaurants(query: String) {
        if (query.isEmpty()) {
            getRestaurants()
            return
        }
        searchRestaurantsUseCase(query).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _uiState.value = RestaurantUiState(restaurants = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _uiState.value = RestaurantUiState(error = result.message ?: "An unexpected error occurred")
                }
                is Resource.Loading -> {
                    _uiState.value = RestaurantUiState(isLoading = true)
                }
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }
}
