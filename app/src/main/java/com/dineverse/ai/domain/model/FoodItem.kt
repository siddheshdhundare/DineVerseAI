package com.dineverse.ai.domain.model

data class FoodItem(
    val id: String,
    val restaurantId: String,
    val categoryId: String,
    val name: String,
    val description: String,
    val price: Double,
    val discountPrice: Double?,
    val imageUrl: String,
    val isVeg: Boolean,
    val isAvailable: Boolean,
    val rating: Double,
    val preparationTime: String,
    val createdAt: String
)
