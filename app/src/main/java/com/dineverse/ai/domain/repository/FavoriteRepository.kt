package com.dineverse.ai.domain.repository

import com.dineverse.ai.domain.model.FavoriteFood
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getAllFavorites(): Flow<List<FavoriteFood>>
    suspend fun addFavorite(favoriteFood: FavoriteFood)
    suspend fun removeFavorite(foodId: String)
    fun isFavorite(foodId: String): Flow<Boolean>
}
