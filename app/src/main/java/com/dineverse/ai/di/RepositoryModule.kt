package com.dineverse.ai.di

import com.dineverse.ai.data.repository.AuthRepositoryImpl
import com.dineverse.ai.data.repository.CartRepositoryImpl
import com.dineverse.ai.data.repository.FavoriteRepositoryImpl
import com.dineverse.ai.data.repository.RestaurantRepositoryImpl
import com.dineverse.ai.domain.repository.AuthRepository
import com.dineverse.ai.domain.repository.CartRepository
import com.dineverse.ai.domain.repository.FavoriteRepository
import com.dineverse.ai.domain.repository.RestaurantRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindRestaurantRepository(
        restaurantRepositoryImpl: RestaurantRepositoryImpl
    ): RestaurantRepository

    @Binds
    @Singleton
    abstract fun bindCartRepository(
        cartRepositoryImpl: CartRepositoryImpl
    ): CartRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(
        favoriteRepositoryImpl: FavoriteRepositoryImpl
    ): FavoriteRepository
}
