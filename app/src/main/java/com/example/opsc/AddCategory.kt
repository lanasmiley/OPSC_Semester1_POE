package com.example.opsc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

import android.widget.ImageButton



class AddCategory : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        val buttonNext: Button = findViewById(R.id.buttonNext)

            buttonNext.setOnClickListener {
            navigateToCategory()
        }



        // Find the ImageButtons in your layout
        val imageButtonCBD: ImageButton = findViewById(R.id.imageButton_cbd)
        val imageButtonTHC: ImageButton = findViewById(R.id.imageButton_thc)
        val imageButtonEdibles: ImageButton = findViewById(R.id.imageButton_edibles)
        val imageButtonOther: ImageButton = findViewById(R.id.imageButton_other)

        // Set click listeners for the ImageButtons
        imageButtonCBD.setOnClickListener {
            navigateToCategoryPage("CBD")
        }

        imageButtonTHC.setOnClickListener {
            navigateToCategoryPage("THC")
        }

        imageButtonEdibles.setOnClickListener {
            navigateToCategoryPage("Edibles")
        }

        imageButtonOther.setOnClickListener {
            navigateToCategoryPage("Other")
        }
    }

    private fun navigateToCategory() {
        val intent = Intent(this, Categories::class.java)
        startActivity(intent)    }


    private fun navigateToCategoryPage(category: String) {
        // Handle the navigation to the category page based on the selected category
        // You can use Intent or any other navigation mechanism here
        // Example using Intent:
        when (category) {
            "CBD" -> {
                val intent = Intent(this, CBD::class.java)
                startActivity(intent)
            }
            "THC" -> {
                val intent = Intent(this, THC::class.java)
                startActivity(intent)
            }
            "Edibles" -> {
                val intent = Intent(this, Edibles::class.java)
                startActivity(intent)
            }
            "Other" -> {
                val intent = Intent(this, Other::class.java)
                startActivity(intent)
            }
        }
    }


}