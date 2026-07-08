package com.dineverse.ai.domain.usecase

import com.dineverse.ai.core.common.Resource
import com.dineverse.ai.domain.model.FoodItem
import com.dineverse.ai.domain.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFoodItemByIdUseCase @Inject constructor(
    private val repository: RestaurantRepository
) {
    operator fun invoke(id: String): Flow<Resource<FoodItem>> {
        return repository.getFoodItemById(id)
    }
}
