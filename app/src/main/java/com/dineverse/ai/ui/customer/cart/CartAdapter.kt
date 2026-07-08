package com.dineverse.ai.ui.customer.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.dineverse.ai.databinding.ItemCartBinding
import com.dineverse.ai.domain.model.CartItem

class CartAdapter(
    private val onQuantityChanged: (String, Int) -> Unit,
    private val onDeleteClicked: (String) -> Unit
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartItem) {
            binding.apply {
                tvFoodName.text = item.foodName
                tvPrice.text = root.context.getString(com.dineverse.ai.R.string.price_format, item.price)
                tvQuantity.text = item.quantity.toString()
                
                ivVegBadge.setImageResource(
                    if (item.isVeg) android.R.drawable.presence_online 
                    else android.R.drawable.presence_busy
                )
                
                ivFood.load(item.foodImage) {
                    crossfade(true)
                }

                btnPlus.setOnClickListener {
                    onQuantityChanged(item.foodId, item.quantity + 1)
                }

                btnMinus.setOnClickListener {
                    if (item.quantity > 1) {
                        onQuantityChanged(item.foodId, item.quantity - 1)
                    }
                }

                btnDelete.setOnClickListener {
                    onDeleteClicked(item.foodId)
                }
            }
        }
    }

    class CartDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.foodId == newItem.foodId
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }
}
