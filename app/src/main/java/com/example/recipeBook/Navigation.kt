package com.example.recipeBook


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.core.navigation.MainScreenContent
import com.example.feature_add_recipe.CreateRoute
import com.example.feature_add_recipe.EditRoute
import com.example.feature_book.BookRoute
import com.example.feature_find.FindRoute
import com.example.feature_recipe.RecipeDetailsScreen

@Composable
fun MainContentNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composableWithAnimations(MainScreenContent.Create.route) {
            CreateRoute(
                nameScreen = MainScreenContent.Create.nameScreen
            )
        }

        composableWithAnimations(MainScreenContent.Find.route) {
            FindRoute(
                nameScreen = MainScreenContent.Find.nameScreen,
                navController = navController
            )
        }

        composableWithAnimations(MainScreenContent.Book.route) {
            BookRoute(
                nameScreen = MainScreenContent.Book.nameScreen,
                navController = navController
            )
        }

        composable(
            route = "recipeDetail/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.LongType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getLong("recipeId") ?: -1
            RecipeDetailsScreen(recipeId = recipeId,navController = navController)
        }

        composable(
            route = "recipeEdit/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.LongType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getLong("recipeId") ?: -1
            EditRoute(
                nameScreen = MainScreenContent.Create.nameScreen,
                recipeId = recipeId)
        }
    }
}