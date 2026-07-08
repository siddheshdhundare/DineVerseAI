package com.dineverse.ai.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FoodItemDto(
    @SerialName("id")
    val id: String,
    @SerialName("restaurant_id")
    val restaurantId: String,
    @SerialName("category_id")
    val categoryId: String,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String? = null,
    @SerialName("price")
    val price: Double,
    @SerialName("discount_price")
    val discountPrice: Double? = null,
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("is_veg")
    val isVeg: Boolean? = null,
    @SerialName("is_available")
    val isAvailable: Boolean? = null,
    @SerialName("rating")
    val rating: Double? = null,
    @SerialName("preparation_time")
    val preparationTime: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null
)
