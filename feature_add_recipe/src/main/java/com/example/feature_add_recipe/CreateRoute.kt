package com.example.feature_add_recipe

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.db.dto.Recipe
import com.example.feature_recipe.RecipeViewModel
import kotlinx.coroutines.launch

@Composable
fun CreateRoute(
    nameScreen: String,
    // coroutineScope: CoroutineScope = rememberCoroutineScope(),
    viewModel: com.example.feature_recipe.RecipeViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var ingredients by remember { mutableStateOf(mutableListOf<Pair<String, String>>()) }
    var preparation by remember { mutableStateOf(TextFieldValue("")) }
    var photo by remember { mutableStateOf<Int?>(null) }
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF6200EE))
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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {


        // Название
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Название") },
            modifier = Modifier.fillMaxWidth()
        )

        // Таблица ингредиентов
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Ингредиенты",
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            ingredients.forEachIndexed { index, (name, quantity) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { newName ->
                            ingredients[index] = newName to quantity
                        },
                        label = { Text("Ингредиент") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { newQuantity ->
                            ingredients[index] = name to newQuantity
                        },
                        label = { Text("Количество") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Button(
                onClick = { ingredients.add("" to "") },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Добавить ингредиент")
            }
        }

        // Способ приготовления
        OutlinedTextField(
            value = preparation,
            onValueChange = { preparation = it },
            label = { Text("Способ приготовления") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            maxLines = 5
        )

        // Фото
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Фото рецепта", fontSize = 18.sp, color = Color.Black)
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                if (photo != null) {
                    Image(
                        painter = painterResource(id = photo!!),
                        contentDescription = "Фото рецепта",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = "Нет изображения",
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }
            }
            Button(onClick = { /* TODO: Добавить логику выбора фото */ }) {
                Text("Добавить фото")
            }
        }

        // Кнопка сохранения
        Button(
            onClick = { scope.launch {
                viewModel.addRecipe(Recipe(//14,
                    name = title.text,
                    ingredients = "ingredients",
                    instructions = preparation.text,
                    imageUrl = null))
            }},
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Сохранить рецепт")
        }
    }
}