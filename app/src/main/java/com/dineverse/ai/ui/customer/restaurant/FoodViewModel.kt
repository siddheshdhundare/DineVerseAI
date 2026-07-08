package com.dineverse.ai.ui.customer.restaurant

import androidx.lifecycle.viewModelScope
import com.dineverse.ai.core.base.BaseViewModel
import com.dineverse.ai.core.common.Resource
import com.dineverse.ai.domain.model.CartItem
import com.dineverse.ai.domain.model.FavoriteFood
import com.dineverse.ai.domain.model.FoodItem
import com.dineverse.ai.domain.usecase.GetFoodItemByIdUseCase
import com.dineverse.ai.domain.usecase.cart.AddToCartUseCase
import com.dineverse.ai.domain.usecase.favorite.AddFavoriteUseCase
import com.dineverse.ai.domain.usecase.favorite.IsFavoriteUseCase
import com.dineverse.ai.domain.usecase.favorite.RemoveFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodViewModel @Inject constructor(
    private val getFoodItemByIdUseCase: GetFoodItemByIdUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase
) : BaseViewModel() {

    private val _foodState = MutableStateFlow<Resource<FoodItem>>(Resource.Loading())
    val foodState = _foodState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

    fun getFoodItem(id: String) {
        getFoodItemByIdUseCase(id).onEach { result ->
            _foodState.value = result
            if (result is Resource.Success) {
                checkIfFavorite(id)
            }
        }.launchIn(viewModelScope)
    }

    private fun checkIfFavorite(foodId: String) {
        isFavoriteUseCase(foodId).onEach { 
            _isFavorite.value = it
        }.launchIn(viewModelScope)
    }

    fun toggleFavorite(food: FoodItem) {
        viewModelScope.launch {
            if (_isFavorite.value) {
                removeFavoriteUseCase(food.id)
            } else {
                addFavoriteUseCase(
                    FavoriteFood(
                        foodId = food.id,
                        restaurantId = food.restaurantId,
                        foodName = food.name,
                        foodImage = food.imageUrl,
                        price = food.price,
                        isVeg = food.isVeg
                    )
                )
            }
        }
    }

    fun addToCart(food: FoodItem) {
        viewModelScope.launch {
            addToCartUseCase(
                CartItem(
                    foodId = food.id,
                    restaurantId = food.restaurantId,
                    foodName = food.name,
                    foodImage = food.imageUrl,
                    price = food.price,
                    quantity = 1,
                    isVeg = food.isVeg
                )
            )
        }
    }
}
