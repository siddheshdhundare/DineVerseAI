package com.dineverse.ai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartEntity(
    @PrimaryKey
    val foodId: String,
    val restaurantId: String,
    val foodName: String,
    val foodImage: String,
    val price: Double,
    val quantity: Int,
    val isVeg: Boolean
)
