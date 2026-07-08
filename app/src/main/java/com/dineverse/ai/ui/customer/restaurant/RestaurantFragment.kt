package com.dineverse.ai.ui.customer.restaurant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.dineverse.ai.R
import com.dineverse.ai.core.base.BaseFragment
import com.dineverse.ai.core.common.Resource
import com.dineverse.ai.databinding.FragmentRestaurantBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class RestaurantFragment : BaseFragment<FragmentRestaurantBinding>() {

    private val viewModel: RestaurantViewModel by viewModels()
    private val args: RestaurantFragmentArgs by navArgs()
    
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var foodAdapter: FoodAdapter

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentRestaurantBinding {
        return FragmentRestaurantBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupAdapters()
        setupSearch()
        observeViewModel()
        
        fetchData()
    }

    private fun fetchData() {
        viewModel.getRestaurant(args.restaurantId)
        viewModel.getCategories(args.restaurantId)
        viewModel.getFoodItems(args.restaurantId)
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupAdapters() {
        categoryAdapter = CategoryAdapter { category ->
            viewModel.getFoodItems(args.restaurantId, if (category.id == "all") null else category.id)
        }
        binding.rvCategories.adapter = categoryAdapter

        foodAdapter = FoodAdapter(
            onFoodClick = { food ->
                val action = RestaurantFragmentDirections.actionRestaurantFragmentToFoodDetailsFragment(food.id)
                findNavController().navigate(action)
            },
            onAddToCartClick = { food ->
                viewModel.addToCart(food)
                Toast.makeText(context, "${food.name} added to cart", Toast.LENGTH_SHORT).show()
            },
            onFavoriteClick = { food, isFav ->
                viewModel.toggleFavorite(food, isFav)
            }
        )
        binding.rvFoodItems.adapter = foodAdapter
    }

    private fun setupSearch() {
        binding.etSearchFood.addTextChangedListener { text ->
            viewModel.searchFood(args.restaurantId, text.toString())
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.restaurantState.collect { result ->
                        when (result) {
                            is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                            is Resource.Success -> {
                                result.data?.let { restaurant ->
                                    binding.apply {
                                        tvName.text = restaurant.name
                                        tvDescription.text = restaurant.description
                                        tvRating.text = restaurant.rating.toString()
                                        tvDeliveryTime.text = restaurant.deliveryTime
                                        tvAddress.text = restaurant.address
                                        tvStatus.text = if (restaurant.isOpen) "Open Now" else "Closed"
                                        
                                        val statusColor = if (restaurant.isOpen) {
                                            android.R.color.holo_green_dark
                                        } else {
                                            android.R.color.holo_red_dark
                                        }
                                        tvStatus.setTextColor(requireContext().getColor(statusColor))
                                        
                                        ivBanner.load(restaurant.bannerUrl.ifEmpty { restaurant.imageUrl }) {
                                            crossfade(true)
                                        }
                                    }
                                }
                            }
                            is Resource.Error -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                            }
                            else -> Unit
                        }
                    }
                }

                launch {
                    viewModel.categoriesState.collect { result ->
                        if (result is Resource.Success) {
                            val categories = result.data?.toMutableList() ?: mutableListOf()
                            if (categories.none { it.id == "all" }) {
                                categories.add(0, com.dineverse.ai.domain.model.MenuCategory("all", args.restaurantId, "All", "", 0, ""))
                            }
                            categoryAdapter.submitList(categories)
                        }
                    }
                }

                launch {
                    viewModel.foodItemsState.collect { result ->
                        when (result) {
                            is Resource.Loading -> {
                                if (foodAdapter.itemCount == 0) binding.progressBar.visibility = View.VISIBLE
                            }
                            is Resource.Success -> {
                                binding.progressBar.visibility = View.GONE
                                foodAdapter.submitList(result.data)
                            }
                            is Resource.Error -> {
                                binding.progressBar.visibility = View.GONE
                                Timber.e(result.message)
                            }
                            else -> Unit
                        }
                    }
                }

                launch {
                    viewModel.favoriteIds.collect { ids ->
                        foodAdapter.updateFavoriteIds(ids)
                    }
                }
            }
        }
    }
}
