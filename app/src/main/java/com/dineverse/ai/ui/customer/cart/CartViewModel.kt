package com.dineverse.ai.ui.customer.cart

import androidx.lifecycle.viewModelScope
import com.dineverse.ai.core.base.BaseViewModel
import com.dineverse.ai.domain.model.CartItem
import com.dineverse.ai.domain.usecase.cart.ClearCartUseCase
import com.dineverse.ai.domain.usecase.cart.GetCartItemsUseCase
import com.dineverse.ai.domain.usecase.cart.RemoveFromCartUseCase
import com.dineverse.ai.domain.usecase.cart.UpdateQuantityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    private val updateQuantityUseCase: UpdateQuantityUseCase,
    private val clearCartUseCase: ClearCartUseCase
) : BaseViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems = _cartItems.asStateFlow()

    init {
        observeCart()
    }

    private fun observeCart() {
        getCartItemsUseCase().onEach { items ->
            _cartItems.value = items
        }.launchIn(viewModelScope)
    }

    fun removeItem(foodId: String) {
        viewModelScope.launch {
            removeFromCartUseCase(foodId)
        }
    }

    fun updateQuantity(foodId: String, quantity: Int) {
        viewModelScope.launch {
            updateQuantityUseCase(foodId, quantity)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            clearCartUseCase()
        }
    }
}
