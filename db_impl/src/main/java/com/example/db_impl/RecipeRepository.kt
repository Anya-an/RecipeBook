package com.example.db_impl

import android.content.Context
import com.example.db.RecipeDatabase
import com.example.db.dao.RecipeDao
import com.example.db.dto.Recipe
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepository @Inject constructor(
    private val recipeDao: RecipeDao
) {

    val recipesFlow: Flow<List<Recipe>> = recipeDao.getAllRecipes()

    suspend fun insert(recipe: Recipe) = recipeDao.insert(recipe)

    suspend fun getAllRecipes() = recipeDao.getAllRecipes()

    suspend fun getRecipeById(id: Long) = recipeDao.getRecipeById(id)

    suspend fun update(recipe: Recipe) = recipeDao.update(recipe)

    suspend fun delete(id: Long) = recipeDao.delete(id)
    suspend fun deleteRecipe(recipe: Recipe) = recipeDao.deleteRecipe(recipe)
}
