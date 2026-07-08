package com.dineverse.ai.domain.model

data class FavoriteFood(
    val foodId: String,
    val restaurantId: String,
    val foodName: String,
    val foodImage: String,
    val price: Double,
    val isVeg: Boolean
)
