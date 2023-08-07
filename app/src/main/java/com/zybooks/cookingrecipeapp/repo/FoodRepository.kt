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
        var cuisineId = cuisineDao.addCuisine(Cuisine(text = "Pasta"))
        recipeDao.addRecipe(
            Recipe(
                text = "Pesto Pasta",
                answer = "Pasta, Onion, Oil, Pesto, Seasonings, and Cheese",
                steps = "Boil pasta in water, cook onion in oil and stir pesto in pasta",
                cuisineId = cuisineId
            )
        )
        recipeDao.addRecipe(
            Recipe(
                text = "Chicken Pasta",
                answer = "Pasta, Shredded Chicken, and Cheese",
                steps = "Boil pasta in water, cook chicken and add into pasta with cheese",
                cuisineId = cuisineId
            )
        )

        cuisineId = cuisineDao.addCuisine(Cuisine(text = "Sandwich"))
        recipeDao.addRecipe(
            Recipe(
                text = "Peanut Butter and Jelly Sandwich",
                answer = "Bread, Peanut Butter, and Jelly",
                steps = "Spread peanut butter on one bread and jelly on the other and combine",
                cuisineId = cuisineId
            )
        )

        cuisineDao.addCuisine(Cuisine(text = "Rice"))
        recipeDao.addRecipe(
            Recipe(
                text = "Fried Rice",
                answer = "Rice, Egg, Meat of your choosing, and Soy Sauce",
                steps = "Stir fry rice and add egg and meat after a minute or two, then add soy sauce",
                cuisineId = cuisineId
            )
        )
    }
}
