package com.example.feature_recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.db.dto.Recipe
import com.example.db_impl.RecipeRepository
import com.example.network.RecipeApiService

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: RecipeRepository,
    private val apiService: RecipeApiService
) : ViewModel() {


    val recipesFlow: StateFlow<List<Recipe>> = repository.recipesFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList()
    )

    fun addRecipe(recipe: Recipe) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.insert(recipe)
               // loadRecipes()
            }
            //loadRecipes() // Обновить список после добавления
        }
    }

    private val _recipe = MutableStateFlow<Recipe?>(null)
    val recipe: StateFlow<Recipe?> get() = _recipe

    fun loadRecipeById(recipeId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _recipe.value = repository.getRecipeById(recipeId)
            }
        }
    }

    // Сетевые данные (например, для поиска)
    private val _networkRecipesFlow = MutableStateFlow<List<Recipe>>(emptyList())
    val networkRecipesFlow: StateFlow<List<Recipe>> get() = _networkRecipesFlow

    // Загрузка из сети
    fun fetchRecipesFromNetwork(query: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getRecipes(query)
                _networkRecipesFlow.value = response.results.map { recipeDto ->
                    Recipe(
                        id = recipeDto.id.toLong(),
                        name = recipeDto.title,
                        ingredients = "",
                        instructions = "",
                        imageUrl = recipeDto.image
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}

