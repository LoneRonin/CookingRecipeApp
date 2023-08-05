package com.zybooks.cookingrecipeapp.repo

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zybooks.cookingrecipeapp.model.Recipe

@Dao
interface RecipeDao {
    @Query("SELECT * FROM Recipe WHERE id = :id")
    fun getRecipe(id: Long): LiveData<Recipe?>

    @Query("SELECT * FROM Recipe WHERE cuisine_id = :cuisineId ORDER BY id")
    fun getRecipes(cuisineId: Long): LiveData<List<Recipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRecipe(recipe: Recipe): Long

    @Update
    fun updateRecipe(recipe: Recipe)

    @Delete
    fun deleteRecipe(recipe: Recipe)
}