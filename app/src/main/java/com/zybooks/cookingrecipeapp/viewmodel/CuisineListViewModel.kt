package com.zybooks.cookingrecipeapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.zybooks.cookingrecipeapp.model.Cuisine
import com.zybooks.cookingrecipeapp.repo.FoodRepository

class CuisineListViewModel(application: Application) : AndroidViewModel(application) {

    private val foodRepo = FoodRepository.getInstance(application.applicationContext)

    val cuisineListLiveData: LiveData<List<Cuisine>> = foodRepo.getCuisines()

    fun addCuisine(cuisine: Cuisine) = foodRepo.addCuisine(cuisine)

    fun deleteCuisine(cuisine: Cuisine) = foodRepo.deleteCuisine(cuisine)
}