package com.dineverse.ai.data.repository

import com.dineverse.ai.data.local.dao.FavoriteDao
import com.dineverse.ai.data.mapper.toDomain
import com.dineverse.ai.data.mapper.toEntity
import com.dineverse.ai.domain.model.FavoriteFood
import com.dineverse.ai.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao
) : FavoriteRepository {

    override fun getAllFavorites(): Flow<List<FavoriteFood>> {
        return favoriteDao.getAllFavorites().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addFavorite(favoriteFood: FavoriteFood) {
        favoriteDao.addFavorite(favoriteFood.toEntity())
    }

    override suspend fun removeFavorite(foodId: String) {
        favoriteDao.removeFavorite(foodId)
    }

    override fun isFavorite(foodId: String): Flow<Boolean> {
        return favoriteDao.isFavorite(foodId)
    }
}
