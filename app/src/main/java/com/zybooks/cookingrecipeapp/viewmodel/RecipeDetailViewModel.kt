package com.zybooks.cookingrecipeapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.zybooks.cookingrecipeapp.model.Recipe
import com.zybooks.cookingrecipeapp.repo.FoodRepository

class RecipeDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val foodRepo = FoodRepository.getInstance(application)

    private val recipeIdLiveData = MutableLiveData<Long>()

    val recipeLiveData: LiveData<Recipe> =
        Transformations.switchMap(recipeIdLiveData) { recipeId ->
            foodRepo.getRecipe(recipeId)
        }

    fun loadRecipe(recipeId: Long) {
        recipeIdLiveData.value = recipeId
    }

    fun addRecipe(recipe: Recipe) = foodRepo.addRecipe(recipe)

    fun updateRecipe(recipe: Recipe) = foodRepo.updateRecipe(recipe)
}