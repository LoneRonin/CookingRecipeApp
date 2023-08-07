package com.zybooks.cookingrecipeapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.preference.SwitchPreferenceCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.zybooks.cookingrecipeapp.model.Cuisine
import com.zybooks.cookingrecipeapp.model.Recipe
import com.zybooks.cookingrecipeapp.repo.FoodDatabase
import com.zybooks.cookingrecipeapp.repo.FoodRepository
import com.zybooks.cookingrecipeapp.viewmodel.RecipeListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchActivity : AppCompatActivity() {
    lateinit var searchView: SearchView
    lateinit var listView: ListView
    lateinit var list: ArrayList<String>
    lateinit var adapter: ArrayAdapter<*>

    companion object {
        const val EXTRA_CUISINE_ID = "com.zybooks.cookingrecipeapp.cuisine_id"
        const val EXTRA_CUISINE_TEXT = "com.zybooks.cookingrecipeapp.cuisine_text"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        title = "Search Recipes"
        searchView = findViewById(R.id.searchView)
        listView = findViewById(R.id.listView)
        list = ArrayList()

        val db = Room.databaseBuilder(applicationContext, FoodDatabase::class.java, "food.db").build()
        lifecycleScope.launch {
            val recipes: List<Recipe> = withContext(Dispatchers.IO) {
                db.recipeDao().getAllRecipes()
            }

            for (recipe in recipes) {
                list.add(recipe.text)
            }

        }

        adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedRecipeName = list[position]

            if (selectedRecipeName != null) {
                // Start the RecipeDetailsActivity and pass the selected recipe name
                val intent = Intent(this@SearchActivity, RecipeActivity::class.java)
                intent.putExtra("recipeName", selectedRecipeName)
                //intent.putExtra(EXTRA_CUISINE_ID, recipe.cuisineId)
                startActivity(intent)
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (list.contains(query)) {
                    adapter.filter.filter(query)
                } else {
                    Toast.makeText(this@SearchActivity, "No Match found", Toast.LENGTH_LONG).show()
                }
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
    }
}