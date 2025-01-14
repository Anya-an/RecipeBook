package com.example.feature_recipe

import com.example.db.RecipeDatabase
import com.example.db.dao.RecipeDao
import com.example.db.di.LocalDBModule
import com.example.db_impl.RecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.components.ViewModelComponent
import org.mockito.Mockito.mock
import com.example.feature_recipe.RecipeViewModel
import com.example.network.RecipeApiService
import org.mockito.Mockito.`when`

@Module
@TestInstallIn(
    components = [SingletonComponent::class], // Указываем компонент, в котором будет происходить инжекция
    replaces = [LocalDBModule::class] // Заменяем реальный модуль на тестовый
)
object TestLocalDBModule {

    @Provides
    fun provideMockRecipeDatabase(): RecipeDatabase {
        return mock(RecipeDatabase::class.java) // Мок для базы данных
    }

    @Provides
    fun provideMockRecipeDao(recipeDatabase: RecipeDatabase): RecipeDao {
        val mockDao = mock(RecipeDao::class.java)
        `when`(recipeDatabase.recipeDao()).thenReturn(mockDao) // Мок для DAO
        return mockDao
    }
}