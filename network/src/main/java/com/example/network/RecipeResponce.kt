package com.example.network

data class RecipeResponse(
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
