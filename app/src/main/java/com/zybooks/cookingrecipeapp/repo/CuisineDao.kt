package com.zybooks.cookingrecipeapp.repo

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zybooks.cookingrecipeapp.model.Cuisine

@Dao
interface CuisineDao {
    @Query("SELECT * FROM Cuisine WHERE id = :id")
    fun getCuisine(id: Long): LiveData<Cuisine?>

    @Query("SELECT * FROM Cuisine ORDER BY text COLLATE NOCASE")
    fun getCuisines(): LiveData<List<Cuisine>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCuisine(cuisine: Cuisine): Long

    @Update
    fun updateCuisine(cuisine: Cuisine)

    @Delete
    fun deleteCuisine(cuisine: Cuisine)
}