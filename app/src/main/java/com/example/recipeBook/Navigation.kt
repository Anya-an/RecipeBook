package com.example.recipeBook


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.core.navigation.MainScreenContent
import com.example.feature_add_recipe.CreateRoute
import com.example.feature_find.FindRoute

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
                nameScreen = MainScreenContent.Find.nameScreen
            )
        }

        composableWithAnimations(MainScreenContent.Book.route) {
            FindRoute(
                nameScreen = MainScreenContent.Book.nameScreen
            )
        }
    }
}