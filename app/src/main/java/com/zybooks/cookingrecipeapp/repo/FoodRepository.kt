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

        cuisineId = cuisineDao.addCuisine(Cuisine(text = "Rice"))
        recipeDao.addRecipe(
            Recipe(
                text = "Fried Rice",
                answer = "Rice, Egg, Meat of your choosing, and Soy Sauce",
                steps = "Stir fry rice and add egg and meat after a minute or two, then add soy sauce",
                cuisineId = cuisineId
            )
        )
        recipeDao.addRecipe(
            Recipe(
                text = "Arroz Caldo",
                answer = "Rice, Meat of your choosing, Seasoning, and Broth",
                steps = "Boil rice in broth and add any meats and seasoning to boil",
                cuisineId = cuisineId
            )
        )

        cuisineId = cuisineDao.addCuisine(Cuisine(text = "Potato"))
        recipeDao.addRecipe(
            Recipe(
                text = "Mashed Potato",
                answer = "Potatoes, Salt, Pepper, and Butter",
                steps = "Boil potatoes, strain, mash, and add salt, pepper, and butter",
                cuisineId = cuisineId
            )
        )
        recipeDao.addRecipe(
            Recipe(
                text = "French Fries",
                answer = "Potatoes and Salt",
                steps = "Peel Potato and cut into thin pieces, then put in fryer and add salt",
                cuisineId = cuisineId
            )
        )

        cuisineId = cuisineDao.addCuisine(Cuisine(text = "Milk Shake"))
        recipeDao.addRecipe(
            Recipe(
                text = "Strawberry Shake",
                answer = "Strawberries, Vanilla Ice Cream, and Milk",
                steps = "Put all ingredients in a blender for 2-3 minutes",
                cuisineId = cuisineId
            )
        )
        recipeDao.addRecipe(
            Recipe(
                text = "Chocolate Shake",
                answer = "Chocolate Ice Cream and Milk",
                steps = "Put all ingredients in a blender for 2-3 minutes",
                cuisineId = cuisineId
            )
        )

        cuisineId = cuisineDao.addCuisine(Cuisine(text = "Chicken"))
        recipeDao.addRecipe(
            Recipe(
                text = "Fried Chicken",
                answer = "Chicken, Salt, and Pepper",
                steps = "Season chicken and add to fryer",
                cuisineId = cuisineId
            )
        )
        recipeDao.addRecipe(
            Recipe(
                text = "Grilled Chicken",
                answer = "Chicken, Salt, and Pepper",
                steps = "Season chicken and add to grill",
                cuisineId = cuisineId
            )
        )

        cuisineId = cuisineDao.addCuisine(Cuisine(text = "Soup"))
        recipeDao.addRecipe(
            Recipe(
                text = "Beef Soup",
                answer = "Beef, Cabbage, String Beans, Plantain, and Corn",
                steps = "Boil beef and add vegetables",
                cuisineId = cuisineId
            )
        )
        recipeDao.addRecipe(
            Recipe(
                text = "Sinigang",
                answer = "Tamarind, Okra, Fish, and Bok Choy",
                steps = "Boil Fish and add vegetables",
                cuisineId = cuisineId
            )
        )

        cuisineId = cuisineDao.addCuisine(Cuisine(text = "Hamburger"))
        recipeDao.addRecipe(
            Recipe(
                text = "Cheeseburger",
                answer = "Hanburger Buns, Beef Patty, and Cheese",
                steps = "Grill beef patty in a grill, add cheese on top and put in hamburger buns",
                cuisineId = cuisineId
            )
        )
        recipeDao.addRecipe(
            Recipe(
                text = "Veggie Burger",
                answer = "Lettuce, Peef Patty, Tomatoes, Onions",
                steps = "Grill beef patty in a grill, add other ingredients and wrap in lettuce",
                cuisineId = cuisineId
            )
        )
    }
}
