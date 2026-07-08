package com.dineverse.ai.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dineverse.ai.data.local.dao.CartDao
import com.dineverse.ai.data.local.dao.FavoriteDao
import com.dineverse.ai.data.local.entity.CartEntity
import com.dineverse.ai.data.local.entity.FavoriteEntity

@Database(
    entities = [CartEntity::class, FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun favoriteDao(): FavoriteDao
}
