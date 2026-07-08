package com.dineverse.ai.data.repository

import com.dineverse.ai.data.local.dao.CartDao
import com.dineverse.ai.data.mapper.toDomain
import com.dineverse.ai.data.mapper.toEntity
import com.dineverse.ai.domain.model.CartItem
import com.dineverse.ai.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao
) : CartRepository {

    override fun getCartItems(): Flow<List<CartItem>> {
        return cartDao.getAllCartItems().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addToCart(cartItem: CartItem) {
        val existingItem = cartDao.getCartItemById(cartItem.foodId)
        if (existingItem != null) {
            cartDao.updateCartItem(existingItem.copy(quantity = existingItem.quantity + cartItem.quantity))
        } else {
            cartDao.insertCartItem(cartItem.toEntity())
        }
    }

    override suspend fun removeFromCart(foodId: String) {
        cartDao.deleteCartItem(foodId)
    }

    override suspend fun updateQuantity(foodId: String, quantity: Int) {
        val existingItem = cartDao.getCartItemById(foodId)
        if (existingItem != null) {
            if (quantity > 0) {
                cartDao.updateCartItem(existingItem.copy(quantity = quantity))
            } else {
                cartDao.deleteCartItem(foodId)
            }
        }
    }

    override suspend fun clearCart() {
        cartDao.clearCart()
    }
}
