package com.dineverse.ai.ui.customer.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dineverse.ai.R
import com.dineverse.ai.core.base.BaseFragment
import com.dineverse.ai.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var restaurantAdapter: RestaurantListAdapter

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListeners()
        observeUiState()
    }

    private fun setupRecyclerView() {
        restaurantAdapter = RestaurantListAdapter { restaurant ->
            val action = HomeFragmentDirections.actionHomeFragmentToRestaurantFragment(restaurant.id)
            findNavController().navigate(action)
        }
        binding.rvRestaurants.adapter = restaurantAdapter
    }

    private fun setupListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getRestaurants()
        }

        binding.btnRetry.setOnClickListener {
            viewModel.getRestaurants()
        }

        binding.etSearch.addTextChangedListener { text ->
            viewModel.searchRestaurants(text.toString())
        }

        binding.btnCart.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_cartFragment)
        }

        binding.btnFavorite.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_favoriteFragment)
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.swipeRefresh.isRefreshing = state.isLoading
                    binding.progressBar.visibility = if (state.isLoading && state.restaurants.isEmpty()) View.VISIBLE else View.GONE
                    
                    if (state.error != null) {
                        binding.layoutError.visibility = View.VISIBLE
                        binding.tvError.text = state.error
                        binding.rvRestaurants.visibility = View.GONE
                        binding.layoutEmpty.visibility = View.GONE
                    } else if (!state.isLoading && state.restaurants.isEmpty()) {
                        binding.layoutError.visibility = View.GONE
                        binding.rvRestaurants.visibility = View.GONE
                        binding.layoutEmpty.visibility = View.VISIBLE
                    } else {
                        binding.layoutError.visibility = View.GONE
                        binding.layoutEmpty.visibility = View.GONE
                        binding.rvRestaurants.visibility = View.VISIBLE
                        restaurantAdapter.submitList(state.restaurants)
                    }
                }
            }
        }
    }
}
