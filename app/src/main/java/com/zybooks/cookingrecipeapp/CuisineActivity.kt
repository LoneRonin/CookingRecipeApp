package com.zybooks.cookingrecipeapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zybooks.cookingrecipeapp.model.Cuisine
import com.zybooks.cookingrecipeapp.viewmodel.CuisineListViewModel
import androidx.lifecycle.ViewModelProvider

class CuisineActivity : AppCompatActivity(),
    CuisineDialogFragment.OnCuisineEnteredListener {

    private var cuisineAdapter = CuisineAdapter(mutableListOf())
    private lateinit var cuisineRecyclerView: RecyclerView
    private lateinit var cuisineColors: IntArray
    private val cuisineListViewModel: CuisineListViewModel by lazy {
        ViewModelProvider(this).get(CuisineListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cuisine)

        cuisineListViewModel.cuisineListLiveData.observe(
            this, { cuisineList ->
                updateUI(cuisineList)
            })

        cuisineColors = resources.getIntArray(R.array.cuisineColors)

        findViewById<FloatingActionButton>(R.id.add_cuisine_button).setOnClickListener {
            addCuisineClick() }

        cuisineRecyclerView = findViewById(R.id.cuisine_recycler_view)
        cuisineRecyclerView.layoutManager = GridLayoutManager(applicationContext, 2)
    }

    private fun updateUI(cuisineList: List<Cuisine>) {
        cuisineAdapter = CuisineAdapter(cuisineList as MutableList<Cuisine>)
        cuisineRecyclerView.adapter = cuisineAdapter
    }

    override fun onCuisineEntered(cuisineText: String) {
        if (cuisineText.isNotEmpty()) {
            val cuisine = Cuisine(0, cuisineText)
            cuisineListViewModel.addCuisine(cuisine)

            Toast.makeText(this, "Added $cuisineText", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addCuisineClick() {
        val dialog = CuisineDialogFragment()
        dialog.show(supportFragmentManager, "cuisineDialog")
    }

    private inner class CuisineHolder(inflater: LayoutInflater, parent: ViewGroup?) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.recycler_view_items, parent, false)),
        View.OnClickListener {

        private var cuisine: Cuisine? = null
        private val cuisineTextView: TextView

        init {
            itemView.setOnClickListener(this)
            cuisineTextView = itemView.findViewById(R.id.cuisine_text_view)
        }

        fun bind(cuisine: Cuisine, position: Int) {
            this.cuisine = cuisine
            cuisineTextView.text = cuisine.text

            // Make the background color dependent on the length of the subject string
            val colorIndex = cuisine.text.length % cuisineColors.size
            cuisineTextView.setBackgroundColor(cuisineColors[colorIndex])
        }

        override fun onClick(view: View) {
            // Start QuestionActivity with the selected subject
            val intent = Intent(this@CuisineActivity, RecipeActivity::class.java)
            intent.putExtra(RecipeActivity.EXTRA_CUISINE_ID, cuisine!!.id)
            intent.putExtra(RecipeActivity.EXTRA_CUISINE_TEXT, cuisine!!.text)

            startActivity(intent)
        }
    }

    private inner class CuisineAdapter(private val cuisineList: MutableList<Cuisine>) :
        RecyclerView.Adapter<CuisineHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CuisineHolder {
            val layoutInflater = LayoutInflater.from(applicationContext)
            return CuisineHolder(layoutInflater, parent)
        }

        override fun onBindViewHolder(holder: CuisineHolder, position: Int) {
            holder.bind(cuisineList[position], position)
        }

        override fun getItemCount(): Int {
            return cuisineList.size
        }
    }
}