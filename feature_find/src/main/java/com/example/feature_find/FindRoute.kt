package com.example.feature_find

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feature_recipe.RecipeViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.feature_recipe.RecipeCard
import com.example.db.dto.Recipe


@Composable
fun FindRoute(
    nameScreen: String,
    viewModel: RecipeViewModel = hiltViewModel(),
    navController: NavController,
    // coroutineScope: CoroutineScope = rememberCoroutineScope(),
    //charactersViewModel: CharactersViewModel = hiltViewModel()
) {
    //var searchQuery by remember { mutableStateOf("") }
    //var searchResults by remember { mutableStateOf<List<String>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }

    // Подписка на результаты поиска из ViewModel
    //val searchResults by viewModel.networkRecipesFlow.collectAsState()
    val searchResults by viewModel.localRecipesFlow.collectAsState()


    val focusManager = LocalFocusManager.current
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp) // Паддинг для отступов слева и справа
            .padding(top = 80.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Поле ввода для поиска
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Поиск рецептов") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            trailingIcon = {
                IconButton(
                    onClick = {
                        focusManager.clearFocus()
                        // Логика поиска
                        viewModel.findRecipeByNameLocal(searchQuery)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Искать"
                    )
                }
            }
        )
        // Таблица с результатами поиска
        if (searchResults.isNotEmpty()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {

                items(searchResults) { recipe ->
                    RecipeCard(recipe,
                        onRecipeClick = {  recipeId ->
                            navController.navigate("recipeDetail/$recipeId")})
                }
            }
        } else {
            Text("Нет результатов", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun RecipeItem(recipeName: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
           // .shadow(4.dp, shape = RoundedCornerShape(8.dp)), // Добавление тени
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = recipeName,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


