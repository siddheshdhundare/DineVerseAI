package com.dineverse.ai.domain.repository

import com.dineverse.ai.core.common.Resource
import com.dineverse.ai.domain.model.FoodItem
import com.dineverse.ai.domain.model.MenuCategory
import com.dineverse.ai.domain.model.Restaurant
import kotlinx.coroutines.flow.Flow

interface RestaurantRepository {
    fun getRestaurants(): Flow<Resource<List<Restaurant>>>
    fun getRestaurantById(id: String): Flow<Resource<Restaurant>>
    fun searchRestaurants(query: String): Flow<Resource<List<Restaurant>>>
    fun getCategories(restaurantId: String): Flow<Resource<List<MenuCategory>>>
    fun getFoodItems(restaurantId: String, categoryId: String? = null): Flow<Resource<List<FoodItem>>>
    fun getFoodItemById(id: String): Flow<Resource<FoodItem>>
    fun searchFood(restaurantId: String, query: String): Flow<Resource<List<FoodItem>>>
}
