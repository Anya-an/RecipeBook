package com.example.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.db.dto.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipe")
     fun getAllRecipes(): Flow<List<Recipe>>

    // Получение рецепта по id
    @Query("SELECT * FROM recipe WHERE id = :id LIMIT 1")
    fun getRecipeById(id: Long): Recipe?

    // Вставка нового рецепта
    @Insert
    fun insert(recipe: Recipe)

    // Обновление рецепта
    @Update
    fun update(recipe: Recipe)

    // Удаление рецепта
    @Query("DELETE FROM recipe WHERE id = :id")
    fun delete(id: Long)
}
