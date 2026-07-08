package com.dineverse.ai.di

import android.content.Context
import androidx.room.Room
import com.dineverse.ai.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "dineverse_db"
        ).build()
    }

    @Provides
    fun provideCartDao(db: AppDatabase) = db.cartDao()

    @Provides
    fun provideFavoriteDao(db: AppDatabase) = db.favoriteDao()
}
