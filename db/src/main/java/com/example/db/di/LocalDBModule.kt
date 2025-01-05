package com.example.db.di

import android.content.Context
import androidx.room.Room
import com.example.db.RecipeDatabase
import com.example.db.dao.RecipeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LocalDBModule {

    @Provides
    fun provideRecipeDatabase(
        @ApplicationContext context: Context
    ): RecipeDatabase = Room.databaseBuilder(context, RecipeDatabase::class.java, "recipe_database")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideRecipeDao(
        recipeDatabase: RecipeDatabase
    ): RecipeDao = recipeDatabase.recipeDao()

}