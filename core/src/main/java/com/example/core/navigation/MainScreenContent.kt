package com.example.core.navigation

sealed class MainScreenContent(val route: String,
                               val nameScreen: String
) {
    data object Create : MainScreenContent(
        route = DestinationMainContent.CREATE_RECIPE_ROUTE,
        nameScreen = "Создать рецепт"
    )

    data object Find : MainScreenContent(
        route = DestinationMainContent.FIND_ROUTE,
        nameScreen = "Поиск рецептов"
    )

    data object Book : MainScreenContent(
        route = DestinationMainContent.BOOK_ROUTE,
        nameScreen = "Книга рецептов"
    )
}