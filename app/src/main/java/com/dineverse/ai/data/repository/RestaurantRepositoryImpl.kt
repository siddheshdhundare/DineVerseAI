package com.dineverse.ai.data.repository

import com.dineverse.ai.core.common.Resource
import com.dineverse.ai.data.mapper.toDomain
import com.dineverse.ai.data.remote.datasource.RestaurantRemoteDataSource
import com.dineverse.ai.domain.model.FoodItem
import com.dineverse.ai.domain.model.MenuCategory
import com.dineverse.ai.domain.model.Restaurant
import com.dineverse.ai.domain.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestaurantRepositoryImpl @Inject constructor(
    private val remoteDataSource: RestaurantRemoteDataSource
) : RestaurantRepository {

    override fun getRestaurants(): Flow<Resource<List<Restaurant>>> = flow {
        emit(Resource.Loading())
        try {
            val restaurants = remoteDataSource.getRestaurants().map { it.toDomain() }
            emit(Resource.Success(restaurants))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }

    override fun getRestaurantById(id: String): Flow<Resource<Restaurant>> = flow {
        emit(Resource.Loading())
        try {
            val restaurant = remoteDataSource.getRestaurantById(id).toDomain()
            emit(Resource.Success(restaurant))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }

    override fun searchRestaurants(query: String): Flow<Resource<List<Restaurant>>> = flow {
        emit(Resource.Loading())
        try {
            val restaurants = remoteDataSource.searchRestaurants(query).map { it.toDomain() }
            emit(Resource.Success(restaurants))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }

    override fun getCategories(restaurantId: String): Flow<Resource<List<MenuCategory>>> = flow {
        emit(Resource.Loading())
        try {
            val categories = remoteDataSource.getCategories(restaurantId).map { it.toDomain() }
            emit(Resource.Success(categories))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to fetch categories"))
        }
    }

    override fun getFoodItems(restaurantId: String, categoryId: String?): Flow<Resource<List<FoodItem>>> = flow {
        emit(Resource.Loading())
        try {
            val foodItems = remoteDataSource.getFoodItems(restaurantId, categoryId).map { it.toDomain() }
            emit(Resource.Success(foodItems))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to fetch food items"))
        }
    }

    override fun getFoodItemById(id: String): Flow<Resource<FoodItem>> = flow {
        emit(Resource.Loading())
        try {
            val foodItem = remoteDataSource.getFoodItemById(id).toDomain()
            emit(Resource.Success(foodItem))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to fetch food item"))
        }
    }

    override fun searchFood(restaurantId: String, query: String): Flow<Resource<List<FoodItem>>> = flow {
        emit(Resource.Loading())
        try {
            val foodItems = remoteDataSource.searchFood(restaurantId, query).map { it.toDomain() }
            emit(Resource.Success(foodItems))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Search failed"))
        }
    }
}
