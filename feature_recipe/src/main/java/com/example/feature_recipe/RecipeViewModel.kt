package com.example.feature_recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.db.dto.Recipe
import com.example.db_impl.RecipeRepository
import com.example.network.RecipeApiService
import com.example.network.RecipeInformationResponse

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
    val recipe: StateFlow<Recipe?> = _recipe


    fun loadRecipeById(recipeId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Сначала ищем в локальной базе данных
                val localRecipe = repository.getRecipeById(recipeId)

                if (localRecipe != null) {
                    _recipe.value = localRecipe
                } else {
                    // Если нет, ищем через Retrofit
                    //_recipeInfo.value = repository.fetchRecipeByIdFromApi(recipeId)
                    try {
                        val response = apiService.getRecipeInformation(recipeId)
                        val recipe = Recipe(
                            id = recipeId,
                            name = response.title,
                            ingredients = response.ingredients?.joinToString(", ") { it.name } ?: null,
                            instructions = response.instructions ?: "Нет инструкций",
                            imageUrl = response.image
                        )

                        _recipe.value = recipe
                    } catch (e: Exception) {
                        Log.e("RecipeViewModel", "Error loading recipe: ${e.localizedMessage}")
                    }
                }
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

