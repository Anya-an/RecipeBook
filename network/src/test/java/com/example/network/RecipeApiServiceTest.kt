package com.example.network

import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify
import kotlin.test.assertEquals

class RecipeApiServiceTest {

    private lateinit var apiService: RecipeApiService

    @Before
    fun setUp() {
        // Создание мока для RecipeApiService
        apiService = Mockito.mock(RecipeApiService::class.java)
    }

    @Test
    fun `test getRecipes success`(): Unit = runBlocking{
        // Arrange: Задаем поведение для getRecipes
        val item = RecipeResponceItem(id = 123,
            title = "Spaghetti Carbonara",
            image = "https://example.com/spaghetti.jpg",
            imageType = "jpg")
        val mockResponse = ResipesListResponce(listOf(item),1,1,1)

        Mockito.`when`(apiService.getRecipes("spaghetti", 1)).doReturn(mockResponse)

        // Act: Вызываем метод getRecipes
        val response = apiService.getRecipes("spaghetti", 1)

        // Assert: Проверяем, что данные возвращены корректно
        assertEquals(1, response.results.size)
        assertEquals("Spaghetti Carbonara", response.results[0].title)
        assertEquals("https://example.com/spaghetti.jpg", response.results[0].image)

        // Дополнительно проверяем, что метод был вызван с нужными параметрами
        verify(apiService).getRecipes("spaghetti", 1)
    }

    @Test
    fun `test getRecipeInformation success`() = runBlocking {
        // Arrange: Задаем поведение для getRecipeInformation
        val mockResponse = RecipeInformationResponse(
            title = "Spaghetti Carbonara",
            ingredients = listOf(Ingredient("name",10f,"mg")),
            instructions = "Cook",
            image = "https://example.com/spaghetti.jpg"
        )
        Mockito.`when`(apiService.getRecipeInformation(123)).doReturn(mockResponse)

        // Act: Вызываем метод getRecipeInformation
        val response = apiService.getRecipeInformation(123)

        // Assert: Проверяем, что данные возвращены корректно
        assertEquals("Spaghetti Carbonara", response.title)
        assertEquals("https://example.com/spaghetti.jpg", response.image)
    }
}
