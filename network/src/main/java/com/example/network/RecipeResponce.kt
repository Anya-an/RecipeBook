package com.example.network

data class ResipesListResponce(
    val results: List<RecipeResponceItem>,
    val offset: Int,
    val number: Int,
    val totalResults: Int
)

data class RecipeResponceItem(
    val id: Int,
    val title: String,
    val image: String,
    val imageType: String
)

data class RecipeInformationResponse(
    val title: String,
    val ingredients: List<Ingredient>,
    val instructions: String,
    val image: String,
)

data class Ingredient(
    val name: String,
    val amount: Float,
    val unit: String
)
