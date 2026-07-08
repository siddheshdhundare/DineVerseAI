package com.dineverse.ai.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuCategoryDto(
    @SerialName("id")
    val id: String,
    @SerialName("restaurant_id")
    val restaurantId: String,
    @SerialName("name")
    val name: String,
    @SerialName("icon")
    val icon: String? = null,
    @SerialName("sort_order")
    val sortOrder: Int? = null,
    @SerialName("created_at")
    val createdAt: String? = null
)
