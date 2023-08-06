package com.zybooks.cookingrecipeapp.repo

import androidx.lifecycle.LiveData
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

    /*init {
        addStarterData()
    }*/

    fun getCuisine(cuisineId: Long): LiveData<Cuisine?> = cuisineDao.getCuisine(cuisineId)

    fun getCuisines(): LiveData<List<Cuisine>> = cuisineDao.getCuisines()

    fun addCuisine(cuisine: Cuisine) {
        cuisine.id = cuisineDao.addCuisine(cuisine)
    }

    fun deleteCuisine(cuisine: Cuisine) = cuisineDao.deleteCuisine(cuisine)

    fun getRecipe(recipeId: Long): LiveData<Recipe?> = recipeDao.getRecipe(recipeId)

    fun getRecipes(cuisineId: Long): LiveData<List<Recipe>> = recipeDao.getRecipes(cuisineId)

    fun addRecipe(recipe: Recipe) {
        recipe.id = recipeDao.addRecipe(recipe)
    }

    fun updateRecipe(recipe: Recipe) = recipeDao.updateRecipe(recipe)

    fun deleteRecipe(recipe: Recipe) = recipeDao.deleteRecipe(recipe)

    private fun addStarterData() {
        var cuisineId = cuisineDao.addCuisine(Cuisine(text = "Pizza"))
        recipeDao.addRecipe(
            Recipe(
                text = "Cheese Pizza",
                answer = "Pizza with Cheese",
                cuisineId = cuisineId
            )
        )
        recipeDao.addRecipe(
            Recipe(
                text = "Pepperoni Pizza",
                answer = "Pizza with Cheese and Pepperoni",
                cuisineId = cuisineId
            )
        )

        cuisineId = cuisineDao.addCuisine(Cuisine(text = "Hamburger"))
        recipeDao.addRecipe(
            Recipe(
                text = "Cheeseburger",
                answer = "Hamburger with Cheese",
                cuisineId = cuisineId
            )
        )

        cuisineDao.addCuisine(Cuisine(text = "Chicken"))
    }
}
