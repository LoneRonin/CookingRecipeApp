package com.zybooks.cookingrecipeapp.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(foreignKeys = [
    ForeignKey(entity = Cuisine::class,
        parentColumns = ["id"],
        childColumns = ["cuisine_id"],
        onDelete = CASCADE)
])
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    var text: String = "",
    var answer: String = "",
    var steps: String = "",

    @ColumnInfo(name = "cuisine_id")
    var cuisineId: Long = 0)
