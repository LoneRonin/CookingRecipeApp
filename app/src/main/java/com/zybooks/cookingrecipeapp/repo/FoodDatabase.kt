package com.zybooks.cookingrecipeapp.repo

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zybooks.cookingrecipeapp.model.Recipe
import com.zybooks.cookingrecipeapp.model.Cuisine

@Database(entities = [Recipe::class, Cuisine::class], version = 1)
abstract class FoodDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao
    abstract fun cuisineDao(): CuisineDao
}