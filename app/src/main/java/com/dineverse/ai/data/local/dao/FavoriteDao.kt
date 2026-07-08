package com.dineverse.ai.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dineverse.ai.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE foodId = :foodId")
    suspend fun removeFavorite(foodId: String)

    @Query("SELECT EXISTS(SELECT * FROM favorites WHERE foodId = :foodId)")
    fun isFavorite(foodId: String): Flow<Boolean>
}
