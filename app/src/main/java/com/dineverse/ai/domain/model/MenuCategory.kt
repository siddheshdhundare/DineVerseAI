package com.dineverse.ai.domain.model

data class MenuCategory(
    val id: String,
    val restaurantId: String,
    val name: String,
    val icon: String,
    val sortOrder: Int,
    val createdAt: String
)
