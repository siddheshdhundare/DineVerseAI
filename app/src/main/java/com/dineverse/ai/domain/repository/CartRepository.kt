package com.dineverse.ai.domain.repository

import com.dineverse.ai.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartItems(): Flow<List<CartItem>>
    suspend fun addToCart(cartItem: CartItem)
    suspend fun removeFromCart(foodId: String)
    suspend fun updateQuantity(foodId: String, quantity: Int)
    suspend fun clearCart()
}
