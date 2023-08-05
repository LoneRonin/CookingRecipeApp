package com.zybooks.cookingrecipeapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.zybooks.cookingrecipeapp.model.Recipe
import com.zybooks.cookingrecipeapp.repo.FoodRepository

class RecipeListViewModel(application: Application) : AndroidViewModel(application) {

    private val foodRepo = FoodRepository.getInstance(application)

    private val cuisineIdLiveData = MutableLiveData<Long>()

    val recipeListLiveData: LiveData<List<Recipe>> =
        Transformations.switchMap(cuisineIdLiveData) { cuisineId ->
            foodRepo.getRecipes(cuisineId)
        }

    fun loadRecipes(cuisineId: Long) {
        cuisineIdLiveData.value = cuisineId
    }

    fun addRecipe(recipe: Recipe) = foodRepo.addRecipe(recipe)

    fun deleteRecipe(recipe: Recipe) = foodRepo.deleteRecipe(recipe)
}