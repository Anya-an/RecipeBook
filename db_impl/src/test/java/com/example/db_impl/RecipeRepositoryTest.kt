package com.example.db_impl



import com.example.db.dao.RecipeDao
import com.example.db.dto.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class RecipeRepositoryTest {

    private lateinit var mockRecipeDao: RecipeDao
    private lateinit var recipeRepository: RecipeRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        // Инициализация мока вручную
        mockRecipeDao = Mockito.mock(RecipeDao::class.java)
        recipeRepository = RecipeRepository(mockRecipeDao)

        // Подключаем CoroutineDispatcher для тестов
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `test insert calls dao insert`() = runTest {
        // Создаем тестовый рецепт
        val recipe = Recipe(id = 1, name = "Test Recipe", ingredients = "Test Ingredients", instructions = "Test Instructions", imageUrl = null)

        recipeRepository.insert(recipe)

        // Проверяем, что метод insert был вызван на dao
        verify(mockRecipeDao).insert(recipe)
    }

    @Test
    fun `test getAllRecipes returns correct list from dao`() = runTest {
        // Создаем список рецептов
        val recipeList = listOf(
            Recipe(id = 1, name = "Test Recipe 1", ingredients = "Ingredients 1", instructions = "Instructions 1", imageUrl = null),
            Recipe(id = 2, name = "Test Recipe 2", ingredients = "Ingredients 2", instructions = "Instructions 2", imageUrl = null)
        )

        // Подключаем flow, который будет возвращен из dao
        whenever(mockRecipeDao.getAllRecipes()).thenReturn(flowOf(recipeList))

        // Получаем список рецептов
        val result = recipeRepository.getAllRecipes()

        // Проверяем, что результат соответствует ожидаемому списку
        result.collect { recipes ->
            assertEquals(recipeList, recipes)
        }
    }

    @Test
    fun `test getRecipeById returns correct recipe from dao`() = runTest {
        // Создаем тестовый рецепт
        val recipe = Recipe(id = 1, name = "Test Recipe", ingredients = "Test Ingredients", instructions = "Test Instructions", imageUrl = null)

        // Подключаем dao для возврата конкретного рецепта
        whenever(mockRecipeDao.getRecipeById(1)).thenReturn(recipe)

        // Получаем рецепт по ID
        val result = recipeRepository.getRecipeById(1)

        // Проверяем, что результат соответствует ожидаемому рецепту
        assertEquals(recipe, result)
    }

    @Test
    fun `test update calls dao update`() = runTest {
        // Создаем тестовый рецепт
        val recipe = Recipe(id = 1, name = "Updated Recipe", ingredients = "Updated Ingredients", instructions = "Updated Instructions", imageUrl = null)

        // Выполняем метод update
        recipeRepository.update(recipe)

        // Проверяем, что метод update был вызван на dao
        verify(mockRecipeDao).update(recipe)
    }

    @Test
    fun `test delete calls dao delete`() = runTest {
        val recipeId = 1L

        // Выполняем метод delete
        recipeRepository.delete(recipeId)

        // Проверяем, что метод delete был вызван на dao
        verify(mockRecipeDao).delete(recipeId)
    }

    @After
    fun tearDown() {
        // Очищаем диспетчер после теста
        Dispatchers.resetMain()
    }
}
