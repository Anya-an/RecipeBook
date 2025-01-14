package com.example.feature_book

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.db.dto.Recipe
import kotlinx.coroutines.CoroutineScope
import coil.compose.AsyncImage
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.feature_recipe.RecipeCard

@Composable
fun BookRoute(nameScreen: String,
              coroutineScope: CoroutineScope = rememberCoroutineScope(),
              navController: NavController,// =  rememberNavController(),
              viewModel: com.example.feature_recipe.RecipeViewModel = hiltViewModel()) {
    val recipes by viewModel.recipesFlow.collectAsState()

    //val recipesList = coroutineScope.launch {viewModel.loadRecipes()}
    //val recipes: () -> Unit = { coroutineScope.launch { viewModel.loadRecipes() }}

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp)
    ) {
        Text(
            text = nameScreen,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterStart)
        )
    }
    if (recipes.isEmpty()) {
  // Показать сообщение при пустом списке
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
            .padding(top = 80.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(recipes) { recipe ->
                RecipeCard(recipe,
                    onRecipeClick = { recipeId ->
                        navController.navigate("recipeDetail/$recipeId")})
            }
        }
    }
}

