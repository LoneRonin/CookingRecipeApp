package com.zybooks.cookingrecipeapp.viewmodel

import android.app.Application
import com.zybooks.cookingrecipeapp.model.Recipe
import com.zybooks.cookingrecipeapp.repo.FoodRepository

class RecipeListViewModel(application: Application) {

    private val foodRepo = FoodRepository.getInstance(application.applicationContext)

    fun getRecipes(cuisineId: Long): List<Recipe> = foodRepo.getRecipes(cuisineId)
}