package com.dineverse.ai.domain.model

data class CartItem(
    val foodId: String,
    val restaurantId: String,
    val foodName: String,
    val foodImage: String,
    val price: Double,
    val quantity: Int,
    val isVeg: Boolean
)
