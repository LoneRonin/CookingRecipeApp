package com.zybooks.cookingrecipeapp.repo

import android.content.Context
import androidx.room.Room
import com.zybooks.cookingrecipeapp.model.Recipe
import com.zybooks.cookingrecipeapp.model.Cuisine

class FoodRepository private constructor(context: Context) {

    companion object {
        private var instance: FoodRepository? = null

        fun getInstance(context: Context): FoodRepository {
            if (instance == null) {
                instance = FoodRepository(context)
            }
            return instance!!
        }
    }

    private val database : FoodDatabase = Room.databaseBuilder(
        context.applicationContext,
        FoodDatabase::class.java,
        "food.db"
    )
        .allowMainThreadQueries()
        .build()

    private val cuisineDao = database.cuisineDao()
    private val recipeDao = database.recipeDao()

    init {
        if (cuisineDao.getCuisines().isEmpty()) {
            addStarterData()
        }
    }

    fun getCuisine(cuisineId: Long): Cuisine? = cuisineDao.getCuisine(cuisineId)

    fun getCuisines(): List<Cuisine> = cuisineDao.getCuisines()

    fun addCuisine(cuisine: Cuisine) {
        cuisine.id = cuisineDao.addCuisine(cuisine)
    }

    fun deleteCuisine(cuisine: Cuisine) = cuisineDao.deleteCuisine(cuisine)

    fun getRecipe(recipeId: Long): Recipe? = recipeDao.getRecipe(recipeId)

    fun getRecipes(cuisineId: Long): List<Recipe> = recipeDao.getRecipes(cuisineId)

    fun addRecipe(recipe: Recipe) {
        recipe.id = recipeDao.addRecipe(recipe)
    }

    fun updateRecipe(recipe: Recipe) = recipeDao.updateRecipe(recipe)

    fun deleteRecipe(recipe: Recipe) = recipeDao.deleteRecipe(recipe)

    private fun addStarterData() {
        var cuisineId = cuisineDao.addCuisine(Cuisine(text = "Math"))
        recipeDao.addRecipe(
            Recipe(
                text = "What is 2 + 3?",
                answer = "2 + 3 = 5",
                cuisineId = cuisineId
            )
        )
        recipeDao.addRecipe(
            Recipe(
                text = "What is pi?",
                answer = "The ratio of a circle's circumference to its diameter.",
                cuisineId = cuisineId
            )
        )

        cuisineId = cuisineDao.addCuisine(Cuisine(text = "History"))
        recipeDao.addRecipe(
            Recipe(
                text = "On what date was the U.S. Declaration of Independence adopted?",
                answer = "July 4, 1776",
                cuisineId = cuisineId
            )
        )

        cuisineDao.addCuisine(Cuisine(text = "Computing"))
    }
}