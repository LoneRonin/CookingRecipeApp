package com.zybooks.cookingrecipeapp

import androidx.lifecycle.ViewModelProvider
import com.zybooks.cookingrecipeapp.viewmodel.RecipeDetailViewModel
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zybooks.cookingrecipeapp.model.Recipe

class RecipeEditActivity : AppCompatActivity() {

    private lateinit var recipeEditText: EditText
    private lateinit var answerEditText: EditText
    private var recipeId = 0L
    private lateinit var recipe: Recipe
    private val recipeDetailViewModel: RecipeDetailViewModel by lazy {
        ViewModelProvider(this).get(RecipeDetailViewModel::class.java)
    }

    companion object {
        const val EXTRA_RECIPE_ID = "com.zybooks.cookingrecipeapp.recipe_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_edit)

        recipeEditText = findViewById(R.id.recipe_edit_text)
        answerEditText = findViewById(R.id.answer_edit_text)

        findViewById<FloatingActionButton>(R.id.save_button).setOnClickListener { saveButtonClick() }

        // Get question ID from QuestionActivity
        recipeId = intent.getLongExtra(EXTRA_RECIPE_ID, -1L)

        if (recipeId == -1L) {
            // Add new question
            recipe = Recipe()
            recipe.cuisineId = intent.getLongExtra(RecipeActivity.EXTRA_CUISINE_ID, 0)

            setTitle(R.string.add_recipe)
        } else {
            // Display existing question from ViewModel
            recipeDetailViewModel.loadRecipe(recipeId)
            recipeDetailViewModel.recipeLiveData.observe(this,
                { recipe ->
                    this.recipe = recipe
                    updateUI()
                })

            setTitle(R.string.edit_recipe)
        }
    }

    private fun updateUI() {
        recipeEditText.setText(recipe.text)
        answerEditText.setText(recipe.answer)
    }

    private fun saveButtonClick() {
        recipe.text = recipeEditText.text.toString()
        recipe.answer = answerEditText.text.toString()

        if (recipeId == -1L) {
            recipeDetailViewModel.addRecipe(recipe)
        } else {
            recipeDetailViewModel.updateRecipe(recipe)
        }

        // Send back OK result
        setResult(RESULT_OK)
        finish()
    }
}