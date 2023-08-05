package com.zybooks.cookingrecipeapp

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class CuisineDialogFragment: DialogFragment() {

    interface OnCuisineEnteredListener {
        fun onCuisineEntered(cuisineText: String)
    }

    private lateinit var listener: OnCuisineEnteredListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val cuisineEditText = EditText(requireActivity())
        cuisineEditText.inputType = InputType.TYPE_CLASS_TEXT
        cuisineEditText.maxLines = 1
        return AlertDialog.Builder(requireActivity())
            .setTitle(R.string.cuisine)
            .setView(cuisineEditText)
            .setPositiveButton(R.string.create) { dialog, whichButton ->
                // Notify listener
                val cuisine = cuisineEditText.text.toString()
                listener.onCuisineEntered(cuisine.trim())
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnCuisineEnteredListener
    }
}