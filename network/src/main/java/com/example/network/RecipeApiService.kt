package com.example.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {
    @GET("recipes/complexSearch")
    suspend fun getRecipes(
        @Query("query") query: String,
        @Query("number") number: Int = 20,
        @Query("apiKey") apiKey: String = "9da28d784c144471928821cd1ac7e6eb",
        //@Query("instructionsRequired") instruction: Boolean = true,
        //@Query("fillIngredients") ingradients: Boolean = true
    ): ResipesListResponce

    @GET("recipes/{id}/information")
    suspend fun getRecipeInformation(
        @Path("id") id: Long,
        @Query("apiKey") apiKey: String = "9da28d784c144471928821cd1ac7e6eb"
    ): RecipeInformationResponse
}