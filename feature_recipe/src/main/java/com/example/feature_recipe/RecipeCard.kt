package com.example.feature_recipe

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.db.dto.Recipe

@Composable
fun RecipeCard(recipe: Recipe, onRecipeClick: (Long) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onRecipeClick(recipe.id) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically // Центровка по вертикали
        ) {
            // Проверяем, есть ли изображение
            recipe.imageUrl?.let { imageUrl ->
                Box(
                    modifier = Modifier
                        .size(64.dp) // Размер контейнера
                        //.clip(CircleShape) // Делаем квадратное (по умолчанию  срез)
                        .clip(RoundedCornerShape(2.dp) )

                ) {
                    AsyncImage(
                        model = Uri.parse(imageUrl),
                        contentDescription = "Recipe Image",
                        contentScale = ContentScale.Crop, // Обрезка по большей стороне
                        alignment = Alignment.Center, // Центровка изображения
                        modifier = Modifier.fillMaxSize() // Отступ справа от изображения

                    )
                }
                Spacer(modifier = Modifier.weight(0.1f))
            }


            // Информация о рецепте
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = recipe.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Ingredients: ${recipe.ingredients}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Instructions: ${recipe.instructions}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}