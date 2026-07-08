package com.dineverse.ai.ui.customer.restaurant

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
import androidx.navigation.fragment.navArgs
import coil.load
import com.dineverse.ai.R
import com.dineverse.ai.core.base.BaseFragment
import com.dineverse.ai.core.common.Resource
import com.dineverse.ai.databinding.FragmentFoodDetailsBinding
import com.dineverse.ai.domain.model.FoodItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoodDetailsFragment : BaseFragment<FragmentFoodDetailsBinding>() {

    private val viewModel: FoodViewModel by viewModels()
    private val args: FoodDetailsFragmentArgs by navArgs()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentFoodDetailsBinding {
        return FragmentFoodDetailsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeViewModel()
        
        viewModel.getFoodItem(args.foodId)
    }

    private fun setupUI() {
        binding.fabBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.foodState.collect { result ->
                        if (result is Resource.Success) {
                            result.data?.let { food ->
                                updateUI(food)
                            }
                        }
                    }
                }

                launch {
                    viewModel.isFavorite.collect { isFav ->
                        binding.fabFavorite.setImageResource(
                            if (isFav) android.R.drawable.btn_star_big_on
                            else android.R.drawable.btn_star_big_off
                        )
                    }
                }
            }
        }
    }

    private fun updateUI(food: FoodItem) {
        binding.apply {
            tvFoodName.text = food.name
            tvFoodDescription.text = food.description
            tvPrice.text = getString(R.string.price_format, food.price)
            ivFoodLarge.load(food.imageUrl) {
                crossfade(true)
            }

            btnAddToCart.setOnClickListener {
                viewModel.addToCart(food)
                Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
            }

            fabFavorite.setOnClickListener {
                viewModel.toggleFavorite(food)
            }
        }
    }
}
