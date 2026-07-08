package com.dineverse.ai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey
    val foodId: String,
    val restaurantId: String,
    val foodName: String,
    val foodImage: String,
    val price: Double,
    val isVeg: Boolean
)
