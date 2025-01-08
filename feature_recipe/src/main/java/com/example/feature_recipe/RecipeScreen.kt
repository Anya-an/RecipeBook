package com.example.feature_recipe

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.db.dto.Recipe

@Composable
fun RecipeDetailsScreen(
    //recipe: Recipe, // Ваш объект с данными рецепта
    recipeId: Long,
    viewModel: RecipeViewModel = hiltViewModel()
   // modifier: Modifier = Modifier
) {

    //val recipe by viewModel.loadRecipeById(recipeId).collectAsState(initial = null)

    LaunchedEffect(recipeId) {
        viewModel.loadRecipeById(recipeId)
    }

    // Подписываемся на состояние данных
    val recipe by viewModel.recipe.collectAsState()
    //val recipeRemote by viewModel.recipeInfo.collectAsState()

    // Проверяем, есть ли локальный рецепт, если нет — используем удаленные данные
    //val recipe = recipeLocal ?: recipeRemote


    recipe?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Заголовок
            Text(
                text = it.name,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Изображение, если есть
            it.imageUrl?.let { imageUrl ->
                AsyncImage(
                    model = Uri.parse(imageUrl),
                    contentDescription = "Изображение рецепта",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Список ингредиентов
            it.ingredients?.let{
            Text(
                text = "Ингредиенты:",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
                it.split("\n").forEach { ingredient ->
                Text(
                    text = "• $ingredient",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
                }

            // Описание приготовления
            Text(
                text = "Приготовление:",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = it.instructions,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
