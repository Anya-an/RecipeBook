package com.example.recipeBook

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.example.db.dto.Recipe
import com.example.feature_add_recipe.CreateRoute
import com.example.feature_book.BookRoute
import com.example.feature_recipe.RecipeViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

@HiltAndroidTest
class AppScreenTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this) // Hilt rule для тестов

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockViewModel: RecipeViewModel
    private lateinit var navController: TestNavHostController
    private lateinit var mockRecipesFlow: MutableStateFlow<List<Recipe>>

    @Before
    fun setUp() {
        hiltRule.inject() // Инжекция зависимостей Hilt

        // Мок для ViewModel
        mockViewModel = mock(RecipeViewModel::class.java)

        // Мок для потока рецептов
        mockRecipesFlow = MutableStateFlow(emptyList())

        // Создаём NavController для навигации
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun testCreateAndCheckRecipe_withMockViewModelAndNavController(): Unit = runTest {
        // Устанавливаем Compose с навигационным графом
        composeTestRule.setContent {
            MainContentNavGraph(
                navController = navController,
                startDestination = "MainScreenContent.Find.route"
            )
        }

        // Устанавливаем начальную точку навигации
        navController.setCurrentDestination("MainScreenContent.Find.route")

        // Моделируем переход на экран CreateRoute
        navController.navigate("MainScreenContent.Create.route")

        // Данные тестового рецепта
        val recipeTitle = "Тестовый рецепт"
        val preparationText = "Описание приготовления"

        // Вводим данные в поля на экране CreateRoute
        composeTestRule.onNodeWithText("Название рецепта").performTextInput(recipeTitle)
        composeTestRule.onNodeWithText("Способ приготовления").performTextInput(preparationText)

        // Нажимаем кнопку "Сохранить"
        composeTestRule.onNodeWithText("Сохранить").performClick()

        // Проверяем, что ViewModel вызвала метод добавления рецепта
        verify(mockViewModel).addRecipe(
            argThat { recipe ->
                recipe.name == recipeTitle && recipe.instructions == preparationText
            }
        )

        // Моделируем переход на экран BookRoute
        navController.navigate("MainScreenContent.Book.route")

        // Устанавливаем BookRoute с обновленным Flow рецептов
        composeTestRule.setContent {
            BookRoute(
                nameScreen = "BOOK_ROUTE",
                navController = navController,
                viewModel = mockViewModel
            )
        }

        // Обновляем Flow с рецептами
        mockRecipesFlow.emit(
            listOf(
                Recipe(
                    id = 1,
                    name = recipeTitle,
                    ingredients = "Ингредиенты",
                    instructions = preparationText,
                    imageUrl = null,
                    isSaves = true
                )
            )
        )

        // Проверяем, что новый рецепт отображается в BookRoute
        composeTestRule.onNodeWithText(recipeTitle).assertExists()
    }
}