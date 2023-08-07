package com.zybooks.cookingrecipeapp

import android.annotation.SuppressLint
import com.google.android.material.snackbar.Snackbar
import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zybooks.cookingrecipeapp.model.Recipe
import com.zybooks.cookingrecipeapp.model.Cuisine
import com.zybooks.cookingrecipeapp.viewmodel.RecipeListViewModel

class RecipeActivity : AppCompatActivity() {

    private val recipeListViewModel: RecipeListViewModel by lazy {
        ViewModelProvider(this).get(RecipeListViewModel::class.java)
    }

    private lateinit var stepsTextView: TextView

    private lateinit var deletedRecipe: Recipe
    private lateinit var cuisine: Cuisine
    private lateinit var recipeList: List<Recipe>
    private lateinit var answerLabelTextView: TextView
    private lateinit var answerTextView: TextView
    private lateinit var answerButton: Button
    private lateinit var recipeTextView: TextView
    private lateinit var showRecipeLayout: ViewGroup
    private lateinit var noRecipeLayout: ViewGroup
    private var currentRecipeIndex = 0
    private val addRecipeResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {

            // Display the added question, which will appear at end of list
            currentRecipeIndex = recipeList.size

            Toast.makeText(this, R.string.recipe_added, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val EXTRA_CUISINE_ID = "com.zybooks.cookingrecipeapp.cuisine_id"
        const val EXTRA_CUISINE_TEXT = "com.zybooks.cookingrecipeapp.cuisine_text"
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        stepsTextView = findViewById(R.id.steps_text_view)

        recipeTextView = findViewById(R.id.recipe_text_view)
        answerLabelTextView = findViewById(R.id.answer_label_text_view)
        answerTextView = findViewById(R.id.answer_text_view)
        answerButton = findViewById(R.id.answer_button)
        showRecipeLayout = findViewById(R.id.show_recipe_layout)
        noRecipeLayout = findViewById(R.id.no_recipe_layout)

        // Add click callbacks
        answerButton.setOnClickListener { toggleAnswerVisibility() }
        findViewById<Button>(R.id.add_recipe_button).setOnClickListener { addRecipe() }

        // SubjectActivity should provide the subject ID and text
        val cuisineId = intent.getLongExtra(EXTRA_CUISINE_ID, 0)
        val cuisineText = intent.getStringExtra(EXTRA_CUISINE_TEXT)
        cuisine = Cuisine(cuisineId, cuisineText!!)

        // Get all questions for this subject
        recipeList = emptyList()
        recipeListViewModel.loadRecipes(cuisineId)
        recipeListViewModel.recipeListLiveData.observe(
            this, { recipeList ->
                this.recipeList = recipeList
                updateUI()
            })
        //recipeList = recipeListViewModel.getRecipes(cuisineId)
    }

    private fun updateUI() {
        showRecipe(currentRecipeIndex)

        if (recipeList.isEmpty()) {
            updateAppBarTitle()
            displayRecipe(false)
        } else {
            displayRecipe(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.food_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        //  Determine which app bar item was chosen
        return when (item.itemId) {
            R.id.previous -> {
                showRecipe(currentRecipeIndex - 1)
                true
            }
            R.id.next -> {
                showRecipe(currentRecipeIndex + 1)
                true
            }
            R.id.add -> {
                addRecipe()
                true
            }
            R.id.edit -> {
                editRecipe()
                true
            }
            R.id.delete -> {
                deleteRecipe()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun displayRecipe(display: Boolean) {
        if (display) {
            showRecipeLayout.visibility = View.VISIBLE
            noRecipeLayout.visibility = View.GONE
        } else {
            showRecipeLayout.visibility = View.GONE
            noRecipeLayout.visibility = View.VISIBLE
        }
    }

    private fun updateAppBarTitle() {

        // Display subject and number of questions in app bar
        val title = resources.getString(R.string.recipe_number, cuisine.text,
            currentRecipeIndex + 1, recipeList.size)
        setTitle(title)
    }

    private fun addRecipe() {
        val intent = Intent(this, RecipeEditActivity::class.java)
        intent.putExtra(EXTRA_CUISINE_ID, cuisine.id)
        addRecipeResultLauncher.launch(intent)
    }

    private val editRecipeResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, R.string.recipe_updated, Toast.LENGTH_SHORT).show()
        }
    }

    private fun editRecipe() {
        if (currentRecipeIndex >= 0) {
            val intent = Intent(this, RecipeEditActivity::class.java)
            intent.putExtra(RecipeEditActivity.EXTRA_RECIPE_ID, recipeList[currentRecipeIndex].id)
            editRecipeResultLauncher.launch(intent)
        }
    }

    private fun deleteRecipe() {
        if (currentRecipeIndex >= 0) {
            val recipe = recipeList[currentRecipeIndex]
            recipeListViewModel.deleteRecipe(recipe)

            // Save question in case user wants to undo delete
            deletedRecipe = recipe

            // Show delete message with Undo button
            val snackbar = Snackbar.make(
                findViewById(R.id.coordinator_layout),
                R.string.recipe_deleted, Snackbar.LENGTH_LONG
            )
            snackbar.setAction(R.string.undo) {
                // Add question back
                recipeListViewModel.addRecipe(deletedRecipe)
            }
            snackbar.show()
        }
    }

    private fun showRecipe(recipeIndex: Int) {

        // Show question at the given index
        if (recipeList.isNotEmpty()) {
            var newRecipeIndex = recipeIndex

            if (recipeIndex < 0) {
                newRecipeIndex = recipeList.size - 1
            } else if (recipeIndex >= recipeList.size) {
                newRecipeIndex = 0
            }

            currentRecipeIndex = newRecipeIndex
            updateAppBarTitle()

            val recipe = recipeList[currentRecipeIndex]
            recipeTextView.text = recipe.text
            answerTextView.text = recipe.answer
            //stepsTextView.text = recipe.steps
        } else {
            // No questions yet
            currentRecipeIndex = -1
        }
    }

    private fun toggleAnswerVisibility() {
        if (answerTextView.visibility == View.VISIBLE) {
            answerButton.setText(R.string.show_answer)
            answerTextView.visibility = View.INVISIBLE
            answerLabelTextView.visibility = View.INVISIBLE
            stepsTextView.visibility = View.VISIBLE
        } else {
            answerButton.setText(R.string.hide_answer)
            answerTextView.visibility = View.VISIBLE
            answerLabelTextView.visibility = View.VISIBLE
            stepsTextView.visibility = View.INVISIBLE
        }
    }
}
