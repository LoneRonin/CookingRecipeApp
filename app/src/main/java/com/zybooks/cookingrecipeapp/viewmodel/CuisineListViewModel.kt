package com.zybooks.cookingrecipeapp.viewmodel

import android.app.Application
import com.zybooks.cookingrecipeapp.model.Cuisine
import com.zybooks.cookingrecipeapp.repo.FoodRepository

class CuisineListViewModel(application: Application) {

    private val foodRepo = FoodRepository.getInstance(application.applicationContext)

    fun getCuisines(): List<Cuisine> = foodRepo.getCuisines()

    fun addCuisine(cuisine: Cuisine) = foodRepo.addCuisine(cuisine)
}