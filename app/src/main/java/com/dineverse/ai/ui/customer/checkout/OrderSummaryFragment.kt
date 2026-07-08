package com.dineverse.ai.ui.customer.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dineverse.ai.R
import com.dineverse.ai.core.base.BaseFragment
import com.dineverse.ai.databinding.FragmentOrderSummaryBinding
import com.dineverse.ai.ui.customer.cart.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderSummaryFragment : BaseFragment<FragmentOrderSummaryBinding>() {

    private val viewModel: CartViewModel by viewModels() // Reuse CartViewModel for items

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentOrderSummaryBinding {
        return FragmentOrderSummaryBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnPlaceOrder.setOnClickListener {
            Toast.makeText(context, "Order placed successfully!", Toast.LENGTH_LONG).show()
            viewModel.clearCart()
            findNavController().navigate(R.id.action_orderSummaryFragment_to_homeFragment)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cartItems.collect { items ->
                    if (items.isNotEmpty()) {
                        calculateSummary(items)
                    }
                }
            }
        }
    }

    private fun calculateSummary(items: List<com.dineverse.ai.domain.model.CartItem>) {
        val subtotal = items.sumOf { it.price * it.quantity }
        val deliveryFee = 2.0
        val taxes = subtotal * 0.05 // 5% GST
        val total = subtotal + deliveryFee + taxes

        binding.tvSubtotal.text = getString(R.string.price_format, subtotal)
        binding.tvDeliveryFee.text = getString(R.string.price_format, deliveryFee)
        binding.tvTaxes.text = getString(R.string.price_format, taxes)
        binding.tvTotal.text = getString(R.string.price_format, total)
    }
}
