package com.dineverse.ai.data.mapper

import com.dineverse.ai.data.local.entity.CartEntity
import com.dineverse.ai.data.local.entity.FavoriteEntity
import com.dineverse.ai.domain.model.CartItem
import com.dineverse.ai.domain.model.FavoriteFood

fun CartEntity.toDomain(): CartItem {
    return CartItem(
        foodId = foodId,
        restaurantId = restaurantId,
        foodName = foodName,
        foodImage = foodImage,
        price = price,
        quantity = quantity,
        isVeg = isVeg
    )
}

fun CartItem.toEntity(): CartEntity {
    return CartEntity(
        foodId = foodId,
        restaurantId = restaurantId,
        foodName = foodName,
        foodImage = foodImage,
        price = price,
        quantity = quantity,
        isVeg = isVeg
    )
}

fun FavoriteEntity.toDomain(): FavoriteFood {
    return FavoriteFood(
        foodId = foodId,
        restaurantId = restaurantId,
        foodName = foodName,
        foodImage = foodImage,
        price = price,
        isVeg = isVeg
    )
}

fun FavoriteFood.toEntity(): FavoriteEntity {
    return FavoriteEntity(
        foodId = foodId,
        restaurantId = restaurantId,
        foodName = foodName,
        foodImage = foodImage,
        price = price,
        isVeg = isVeg
    )
}
