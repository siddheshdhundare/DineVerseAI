package com.dineverse.ai.ui.customer.restaurant

import androidx.lifecycle.viewModelScope
import com.dineverse.ai.core.base.BaseViewModel
import com.dineverse.ai.core.common.Resource
import com.dineverse.ai.domain.model.FoodItem
import com.dineverse.ai.domain.model.MenuCategory
import com.dineverse.ai.domain.model.Restaurant
import com.dineverse.ai.domain.usecase.GetCategoriesUseCase
import com.dineverse.ai.domain.usecase.GetFoodItemsUseCase
import com.dineverse.ai.domain.usecase.GetRestaurantByIdUseCase
import com.dineverse.ai.domain.usecase.SearchFoodUseCase
import com.dineverse.ai.domain.usecase.cart.AddToCartUseCase
import com.dineverse.ai.domain.usecase.favorite.AddFavoriteUseCase
import com.dineverse.ai.domain.usecase.favorite.GetAllFavoritesUseCase
import com.dineverse.ai.domain.usecase.favorite.IsFavoriteUseCase
import com.dineverse.ai.domain.usecase.favorite.RemoveFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantViewModel @Inject constructor(
    private val getRestaurantByIdUseCase: GetRestaurantByIdUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getFoodItemsUseCase: GetFoodItemsUseCase,
    private val searchFoodUseCase: SearchFoodUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase,
    private val getAllFavoritesUseCase: GetAllFavoritesUseCase
) : BaseViewModel() {

    private val _restaurantState = MutableStateFlow<Resource<Restaurant>>(Resource.Loading())
    val restaurantState = _restaurantState.asStateFlow()

    private val _categoriesState = MutableStateFlow<Resource<List<MenuCategory>>>(Resource.Loading())
    val categoriesState = _categoriesState.asStateFlow()

    private val _foodItemsState = MutableStateFlow<Resource<List<FoodItem>>>(Resource.Loading())
    val foodItemsState = _foodItemsState.asStateFlow()

    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds = _favoriteIds.asStateFlow()

    init {
        observeFavorites()
    }

    private fun observeFavorites() {
        getAllFavoritesUseCase().onEach { list ->
            _favoriteIds.value = list.map { it.foodId }.toSet()
        }.launchIn(viewModelScope)
    }

    fun getRestaurant(id: String) {
        getRestaurantByIdUseCase(id).onEach { result ->
            _restaurantState.value = result
        }.launchIn(viewModelScope)
    }

    fun getCategories(restaurantId: String) {
        getCategoriesUseCase(restaurantId).onEach { result ->
            _categoriesState.value = result
        }.launchIn(viewModelScope)
    }

    fun getFoodItems(restaurantId: String, categoryId: String? = null) {
        getFoodItemsUseCase(restaurantId, categoryId).onEach { result ->
            _foodItemsState.value = result
        }.launchIn(viewModelScope)
    }

    fun searchFood(restaurantId: String, query: String) {
        if (query.isEmpty()) {
            getFoodItems(restaurantId)
            return
        }
        searchFoodUseCase(restaurantId, query).onEach { result ->
            _foodItemsState.value = result
        }.launchIn(viewModelScope)
    }

    fun addToCart(food: FoodItem) {
        viewModelScope.launch {
            addToCartUseCase(
                com.dineverse.ai.domain.model.CartItem(
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

    fun toggleFavorite(food: FoodItem, isFavorite: Boolean) {
        viewModelScope.launch {
            if (isFavorite) {
                removeFavoriteUseCase(food.id)
            } else {
                addFavoriteUseCase(
                    com.dineverse.ai.domain.model.FavoriteFood(
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
}
