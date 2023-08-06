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
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.graphics.Color
import androidx.preference.PreferenceManager

enum class CuisineSortOrder {
    ALPHABETIC, NEW_FIRST, OLD_FIRST
}

class CuisineActivity : AppCompatActivity(),
    CuisineDialogFragment.OnCuisineEnteredListener {

    private var loadCuisineList = true
    private var cuisineAdapter = CuisineAdapter(mutableListOf())
    private lateinit var cuisineRecyclerView: RecyclerView
    private lateinit var cuisineColors: IntArray
    private lateinit var selectedCuisine: Cuisine
    private var selectedCuisinePosition = RecyclerView.NO_POSITION
    private var actionMode: ActionMode? = null
    private val cuisineListViewModel: CuisineListViewModel by lazy {
        ViewModelProvider(this).get(CuisineListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cuisine)

        cuisineListViewModel.cuisineListLiveData.observe(
            this, { cuisineList ->
                if (loadCuisineList) {
                    updateUI(cuisineList)
                }
            })

        cuisineColors = resources.getIntArray(R.array.cuisineColors)

        findViewById<FloatingActionButton>(R.id.add_cuisine_button).setOnClickListener {
            addCuisineClick() }

        cuisineRecyclerView = findViewById(R.id.cuisine_recycler_view)
        cuisineRecyclerView.layoutManager = GridLayoutManager(applicationContext, 2)
    }

    private fun getCuisinesSortOrder(): CuisineSortOrder {

        // Set sort order from settings
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        val sortOrderPref = sharedPrefs.getString("cuisine_order", "alpha")
        return when (sortOrderPref) {
            "alpha" -> CuisineSortOrder.ALPHABETIC
            "new_first" -> CuisineSortOrder.NEW_FIRST
            else -> CuisineSortOrder.OLD_FIRST
        }
    }

    private fun updateUI(cuisineList: List<Cuisine>) {
        cuisineAdapter = CuisineAdapter(cuisineList as MutableList<Cuisine>)
        cuisineAdapter.sortOrder = getCuisinesSortOrder()
        cuisineRecyclerView.adapter = cuisineAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cuisine_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCuisineEntered(cuisineText: String) {
        if (cuisineText.isNotEmpty()) {
            val cuisine = Cuisine(0, cuisineText)
            // Stop updateUI() from being called
            loadCuisineList = false

            cuisineListViewModel.addCuisine(cuisine)

            // Add subject to RecyclerView
            cuisineAdapter.addCuisine(cuisine)

            Toast.makeText(this, "Added $cuisineText", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addCuisineClick() {
        val dialog = CuisineDialogFragment()
        dialog.show(supportFragmentManager, "cuisineDialog")
    }

    private inner class CuisineHolder(inflater: LayoutInflater, parent: ViewGroup?) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.recycler_view_items, parent, false)),
        View.OnLongClickListener,
        View.OnClickListener {

        private var cuisine: Cuisine? = null
        private val cuisineTextView: TextView

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
            cuisineTextView = itemView.findViewById(R.id.cuisine_text_view)
        }

        fun bind(cuisine: Cuisine, position: Int) {
            this.cuisine = cuisine
            cuisineTextView.text = cuisine.text

            if (selectedCuisinePosition == position) {
                // Make selected subject stand out
                cuisineTextView.setBackgroundColor(Color.RED)
            }
            else {
                // Make the background color dependent on the length of the subject string
                val colorIndex = cuisine.text.length % cuisineColors.size
                cuisineTextView.setBackgroundColor(cuisineColors[colorIndex])
            }
        }

        override fun onClick(view: View) {
            // Start QuestionActivity with the selected subject
            val intent = Intent(this@CuisineActivity, RecipeActivity::class.java)
            intent.putExtra(RecipeActivity.EXTRA_CUISINE_ID, cuisine!!.id)
            intent.putExtra(RecipeActivity.EXTRA_CUISINE_TEXT, cuisine!!.text)

            startActivity(intent)
        }

        override fun onLongClick(view: View): Boolean {
            if (actionMode != null) {
                return false
            }

            selectedCuisine = cuisine!!
            selectedCuisinePosition = absoluteAdapterPosition

            // Re-bind the selected item
            cuisineAdapter.notifyItemChanged(selectedCuisinePosition)

            // Show the CAB
            actionMode = this@CuisineActivity.startActionMode(actionModeCallback)
            return true
        }
    }

    private val actionModeCallback = object : ActionMode.Callback {

        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            // Provide context menu for CAB
            val inflater = mode.menuInflater
            inflater.inflate(R.menu.context_menu, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            if (item.itemId == R.id.delete) {
                // Stop updateUI() from being called
                loadCuisineList = false

                // Delete from ViewModel
                cuisineListViewModel.deleteCuisine(selectedCuisine)

                // Remove from RecyclerView
                cuisineAdapter.removeCuisine(selectedCuisine)

                // Close the CAB
                mode.finish()
                return true
            }

            return false
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            actionMode = null

            // CAB closing, need to deselect item if not deleted
            cuisineAdapter.notifyItemChanged(selectedCuisinePosition)
            selectedCuisinePosition = RecyclerView.NO_POSITION
        }
    }

    private inner class CuisineAdapter(private val cuisineList: MutableList<Cuisine>) :
        RecyclerView.Adapter<CuisineHolder>() {

        var sortOrder: CuisineSortOrder = CuisineSortOrder.ALPHABETIC
            set(value) {
                when (value) {
                    CuisineSortOrder.OLD_FIRST -> cuisineList.sortBy { it.updateTime }
                    CuisineSortOrder.NEW_FIRST -> cuisineList.sortByDescending { it.updateTime }
                    else -> cuisineList.sortWith(compareBy(String.CASE_INSENSITIVE_ORDER, { it.text }))
                }
                field = value
            }

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

        fun addCuisine(cuisine: Cuisine) {

            // Add the new subject at the beginning of the list
            cuisineList.add(0, cuisine)

            // Notify the adapter that item was added to the beginning of the list
            notifyItemInserted(0)

            // Scroll to the top
            cuisineRecyclerView.scrollToPosition(0)
        }

        fun removeCuisine(cuisine: Cuisine) {

            // Find subject in the list
            val index = cuisineList.indexOf(cuisine)
            if (index >= 0) {

                // Remove the subject
                cuisineList.removeAt(index)

                // Notify adapter of subject removal
                notifyItemRemoved(index)
            }
        }
    }
}