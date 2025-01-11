package com.example.feature_recipe

import com.example.db.dto.Recipe
import com.example.db_impl.RecipeRepository
import com.example.network.Ingredient
import com.example.network.RecipeApiService
import com.example.network.RecipeInformationResponse
import com.example.network.ResipesListResponce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class RecipeViewModelTest {

    private lateinit var mockRepository: RecipeRepository
    private lateinit var mockApiService: RecipeApiService
    private lateinit var recipeViewModel: RecipeViewModel
    private val testDispatcher = TestCoroutineDispatcher()  // создаем тестовый диспетчер

    @Before
    fun setUp() {
        // Устанавливаем тестовый диспетчер для Main
        mockRepository = mock()
        mockApiService = mock()
        recipeViewModel = RecipeViewModel(mockRepository, mockApiService)
        Dispatchers.setMain(StandardTestDispatcher())
    }



    @Test
    fun `test addRecipe calls insert method of repository`() = runTest {
        // Мокаем репозиторий и API сервис
        val mockRepository = mock<RecipeRepository>()
        val mockApiService = mock<RecipeApiService>()

        val viewModel = RecipeViewModel(mockRepository, mockApiService)

        val recipe = Recipe(id = 1, name = "Test Recipe", ingredients = "", instructions = "", imageUrl = null)

        // Вызываем addRecipe
        viewModel.addRecipe(recipe)

        // Выполняем все корутины, используя testDispatcher
        advanceUntilIdle()  // Ожидаем завершения всех задач

        // Проверяем, что метод insert был вызван в репозитории
        verify(mockRepository).insert(recipe)
    }

    @After
    fun tearDown() {
        // Сбрасываем диспетчер после теста
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}