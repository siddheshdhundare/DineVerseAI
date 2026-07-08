package com.dineverse.ai.ui.customer.favorite

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
import com.dineverse.ai.databinding.FragmentFavoriteBinding
import com.dineverse.ai.domain.model.FavoriteFood
import com.dineverse.ai.domain.model.FoodItem
import com.dineverse.ai.ui.customer.restaurant.FoodAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>() {

    private val viewModel: FavoriteViewModel by viewModels()
    private lateinit var foodAdapter: FoodAdapter

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentFavoriteBinding {
        return FragmentFavoriteBinding.inflate(inflater, container, false)
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

        foodAdapter = FoodAdapter(
            onFoodClick = { food ->
                // Navigate to food details
                // val action = FavoriteFragmentDirections.actionFavoriteFragmentToFoodDetailsFragment(food.id)
                // findNavController().navigate(action)
            },
            onAddToCartClick = { food ->
                // Add to cart logic could be here too
            },
            onFavoriteClick = { food, isFav ->
                viewModel.toggleFavorite(food.toFavoriteFood(), isFav)
            }
        )

        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = foodAdapter
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favorites.collect { list ->
                    if (list.isEmpty()) {
                        binding.rvFavorites.visibility = View.GONE
                        binding.layoutEmpty.visibility = View.VISIBLE
                    } else {
                        binding.rvFavorites.visibility = View.VISIBLE
                        binding.layoutEmpty.visibility = View.GONE
                        foodAdapter.submitList(list.map { it.toFoodItem() })
                    }
                }
            }
        }
    }

    private fun FavoriteFood.toFoodItem() = FoodItem(
        id = foodId,
        restaurantId = restaurantId,
        categoryId = "",
        name = foodName,
        description = "",
        price = price,
        discountPrice = null,
        imageUrl = foodImage,
        isVeg = isVeg,
        isAvailable = true,
        rating = 0.0,
        preparationTime = "",
        createdAt = ""
    )

    private fun FoodItem.toFavoriteFood() = FavoriteFood(
        foodId = id,
        restaurantId = restaurantId,
        foodName = name,
        foodImage = imageUrl,
        price = price,
        isVeg = isVeg
    )
}
