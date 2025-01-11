package com.example.feature_recipe

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.db.dto.Recipe

@OptIn(ExperimentalMaterial3Api::class)
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
        // Добавляем TopAppBar с кнопкой в правом верхнем углу
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = it.name,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    actions = {
                        if (!it.isSaves) {

                            IconButton(onClick = { viewModel.addRecipe(Recipe(
                                name = it.name,
                                ingredients = it.ingredients,
                                instructions = it.instructions,
                                imageUrl = it.imageUrl,
                                isSaves = true
                            )) }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Сохранить рецепт"
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary // Используем контейнерный цвет
                    )
                    //containerColor = MaterialTheme.colorScheme.primary
                )
            }
        ) { paddingValues ->

            // Основное содержимое экрана
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(top = 16.dp)
            ) {
                // Изображение рецепта, если оно есть
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
                it.ingredients?.let {
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
}