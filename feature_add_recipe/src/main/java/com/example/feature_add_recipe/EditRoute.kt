package com.example.feature_add_recipe

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.db.dto.Recipe
import com.example.feature_recipe.RecipeViewModel
import kotlinx.coroutines.launch
import com.example.core.ImageHelper.Companion.copyImageToInternalStorage
import com.example.ui_kit.R
import kotlinx.coroutines.CoroutineScope

@Composable
fun EditRoute(
    nameScreen: String,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    viewModel: RecipeViewModel = hiltViewModel(),
    recipeId: Long,
) {


    val recipe by viewModel.recipe.collectAsState()

    var title by remember { mutableStateOf(TextFieldValue("")) }
    var ingredients by remember { mutableStateOf(mutableListOf<Pair<String, String>>()) }
    var preparation by remember { mutableStateOf(TextFieldValue("")) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var newIngredientName by remember { mutableStateOf(TextFieldValue("")) }
    var newIngredientQuantity by remember { mutableStateOf(TextFieldValue("")) }
    LaunchedEffect(recipeId) {
        viewModel.loadRecipeById(recipeId)
            //title = TextFieldValue(recipe?.name ?: "")
    }
    LaunchedEffect(recipe) {
        //viewModel.loadRecipeById(recipeId)
        //title = TextFieldValue(recipe?.name ?: "")
        recipe?.let { recipe ->
            title = TextFieldValue(recipe.name)

            ingredients = recipe.ingredients
                ?.split("\n")
                ?.map {
                    val parts = it.split(":").map { part -> part.trim() }
                    parts.getOrElse(0) { "" } to parts.getOrElse(1) { "" }
                }
                ?.toMutableList() ?: mutableListOf()

            preparation = TextFieldValue(recipe.instructions ?: "")
            selectedImageUri = recipe.imageUrl?.let { Uri.parse(it) }

            // Очистка полей для добавления нового ингредиента
            newIngredientName = TextFieldValue("")
            newIngredientQuantity = TextFieldValue("")
        }
    }

    val context = LocalContext.current

    // Лаунчер для выбора изображения
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            selectedImageUri = uri // Сохраняем URI выбранного изображения
        }
    )

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
            .padding(top = 80.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {


        // Название
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(context.getString(R.string.create_recipe_title)) },
            modifier = Modifier.fillMaxWidth()
        )

        // Таблица ингредиентов
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = context.getString(R.string.create_ingredients),
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
                        onValueChange = { newName -> ingredients[index] = newName to quantity },
                        label = { Text(context.getString(R.string.create_ingredient)) },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { newQuantity -> ingredients[index] = name to newQuantity },
                        label = { Text(context.getString(R.string.create_count)) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Поля для добавления нового ингредиента
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newIngredientName,
                    onValueChange = { newIngredientName = it },
                    label = { Text(context.getString(R.string.create_ingredient)) },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = newIngredientQuantity,
                    onValueChange = { newIngredientQuantity = it },
                    label = { Text(context.getString(R.string.create_count)) },
                    modifier = Modifier.weight(1f)
                )
            }

            Button(
                onClick = {
                    if (newIngredientName.text.isNotBlank() && newIngredientQuantity.text.isNotBlank()) {
                        ingredients.add(newIngredientName.text to newIngredientQuantity.text)
                        newIngredientName = TextFieldValue("")
                        newIngredientQuantity = TextFieldValue("")
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(context.getString(R.string.create_add_ingredients))
            }
        }
        //}

        // Способ приготовления
        OutlinedTextField(
            value = preparation,
            onValueChange = { preparation = it },
            label = { Text(context.getString(R.string.create_preparation)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            maxLines = 5
        )

        // Фото
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(context.getString(R.string.crate_photo), fontSize = 18.sp, color = Color.Black)
            selectedImageUri?.let{ uri -> Box(
                modifier = Modifier
                    .size(150.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = context.getString(R.string.crate_photo),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            }
            Button(onClick = { launcher.launch("image/*") },
                modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(context.getString(R.string.create_add_photo))
            }
        }

        // Кнопка сохранения
        Button(
            onClick = {
                coroutineScope.launch {
                    try {
                        val savedImagePath = if (selectedImageUri?.toString() != recipe?.imageUrl) {
                            // Только если пользователь выбрал новое изображение
                            selectedImageUri?.let { uri ->
                                copyImageToInternalStorage(context, uri)
                            }
                        } else {
                            recipe!!.imageUrl
                        }
                        val updatedRecipe = recipe!!.copy(
                            name = title.text,
                            ingredients = ingredients.joinToString("\n") { "${it.first}: ${it.second}" },
                            instructions = preparation.text,
                            imageUrl = savedImagePath,
                            isSaves = true
                        )

                        viewModel.updateRecipe(updatedRecipe)
                        Toast.makeText(context, context.getString(R.string.create_success_toast), Toast.LENGTH_SHORT).show()
                        // Очистка полей
                        title = TextFieldValue("")
                        ingredients.clear()
                        preparation = TextFieldValue("")
                        selectedImageUri = null
                        newIngredientName = TextFieldValue("")
                        newIngredientQuantity = TextFieldValue("")
                    } catch (e: Exception) {
                        Toast.makeText(context, "${context.getString(R.string.create_success_toast)} ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(context.getString(R.string.create_add_recipe))
        }
    }
}