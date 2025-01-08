package com.example.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.db.dao.RecipeDao
import com.example.db.dto.Recipe

@Database(entities = [Recipe::class], version = 3)
abstract class RecipeDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile
        private var INSTANCE: RecipeDatabase? = null

        fun getDatabase(context: Context): RecipeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                   // @ApplicationContext context: Context
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    "recipe_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .addMigrations(MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Добавляем новый столбец imageUrl
                database.execSQL("ALTER TABLE recipe ADD COLUMN imageUrl TEXT")
            }
        }

        val MIGRATION_2_3 = object : Migration(2,3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // В данном случае ничего менять не нужно, так как SQLite хранит `String` и `String?` как `TEXT`.
                // Можно добавить проверку или логирование для подтверждения.
            }
        }
    }


}
