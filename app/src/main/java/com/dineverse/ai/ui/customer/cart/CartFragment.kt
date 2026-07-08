package com.dineverse.ai.ui.customer.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dineverse.ai.R
import com.dineverse.ai.core.base.BaseFragment
import com.dineverse.ai.databinding.FragmentCartBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding>() {

    private val viewModel: CartViewModel by viewModels()
    private lateinit var cartAdapter: CartAdapter

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentCartBinding {
        return FragmentCartBinding.inflate(inflater, container, false)
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

        cartAdapter = CartAdapter(
            onQuantityChanged = { id, qty -> viewModel.updateQuantity(id, qty) },
            onDeleteClicked = { id -> viewModel.removeItem(id) }
        )

        binding.rvCart.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cartAdapter
        }

        binding.btnCheckout.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_orderSummaryFragment)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cartItems.collect { items ->
                    if (items.isEmpty()) {
                        binding.rvCart.visibility = View.GONE
                        binding.cardBottom.visibility = View.GONE
                        binding.layoutEmpty.visibility = View.VISIBLE
                    } else {
                        binding.rvCart.visibility = View.VISIBLE
                        binding.cardBottom.visibility = View.VISIBLE
                        binding.layoutEmpty.visibility = View.GONE
                        cartAdapter.submitList(items)
                        calculateTotal(items)
                    }
                }
            }
        }
    }

    private fun calculateTotal(items: List<com.dineverse.ai.domain.model.CartItem>) {
        val subtotal = items.sumOf { it.price * it.quantity }
        val deliveryFee = 2.0 // Fixed for now
        val total = subtotal + deliveryFee

        binding.tvSubtotal.text = getString(R.string.price_format, subtotal)
        binding.tvDeliveryFee.text = getString(R.string.price_format, deliveryFee)
        binding.tvTotal.text = getString(R.string.price_format, total)
    }
}
