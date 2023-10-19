package com.example.opsc

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.IOException

class Edibles : AppCompatActivity() {

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_IMAGE_SELECT = 2
    }

    private lateinit var imageButtonEdibles: ImageButton
    private lateinit var editTextAmountEdibles: EditText
    private lateinit var editTextCommentsEdibles: EditText
    private lateinit var btnSubmitEdibles: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var imageUri: Uri? = null

    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.data?.let { imageUri ->
                try {
                    val imageBitmap =
                        MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                    imageButtonEdibles.setImageBitmap(imageBitmap)
                    this.imageUri = imageUri
                } catch (e: IOException) {
                    Log.e("Edibles", "Error loading image: ${e.message}")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edibles)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Find views by their IDs
        imageButtonEdibles = findViewById(R.id.imageButtonEdibles)
        editTextAmountEdibles = findViewById(R.id.editTextAmountEdibles)
        editTextCommentsEdibles = findViewById(R.id.editTextCommentsEdibles)
        btnSubmitEdibles = findViewById(R.id.btnSubmitEdibles)

        // Set click listener for image selection button
        imageButtonEdibles.setOnClickListener {
            showImageSelectionOptions()
        }

        // Set click listener for submit button
        btnSubmitEdibles.setOnClickListener {
            // Get the entered data
            val amount: String = editTextAmountEdibles.text.toString()
            val comments: String = editTextCommentsEdibles.text.toString()

            try {
                saveUserInput(comments, amount)
                navigateToCategories()
            } catch (e: Exception) {
                Log.e("Categories", "Error saving user input: ${e.message}")
            }
        }
    }

    private fun navigateToCategories() {
        val intent = Intent(this, Categories::class.java)
        startActivity(intent)
    }
    private fun showImageSelectionOptions() {
        val selectIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val chooserIntent = Intent(Intent.ACTION_CHOOSER)
        chooserIntent.putExtra(Intent.EXTRA_INTENT, selectIntent)
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Select Image Source")

        val mimeTypes = arrayOf("image/jpeg", "image/png")
        chooserIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)

        if (chooserIntent.resolveActivity(packageManager) != null) {
            selectImageLauncher.launch(chooserIntent)
        } else {
            Log.e("Edibles", "No activity found to handle image selection")
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap?
                    imageButtonEdibles.setImageBitmap(imageBitmap)
                    imageUri = null // Reset the image URI since it's a captured image
                }
                REQUEST_IMAGE_SELECT -> {
                    val selectedImageUri: Uri? = data?.data
                    if (selectedImageUri != null) {
                        try {
                            val imageBitmap =
                                MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
                            imageButtonEdibles.setImageBitmap(imageBitmap)
                            imageUri = selectedImageUri // Store the selected image URI
                        } catch (e: IOException) {
                            Log.e("Edibles", "Error loading image: ${e.message}")
                        }
                    }
                }
            }
            // Save user input in Categories page
            val comments: String = editTextCommentsEdibles.text.toString()
            val amount: String = editTextAmountEdibles.text.toString()
            saveUserInputInCategories(comments, amount)
        }
    }

    private fun saveUserInputInCategories(comments: String, amount: String) {
        val userId = auth.currentUser?.uid

        if (userId != null) {
            val userNode = database.reference.child("users").child(userId).child("categories")
            val cbdNode = userNode.child("cbd")
            cbdNode.child("comments").setValue(comments)
            cbdNode.child("amount").setValue(amount)

            val ediblesNode = userNode.child("edibles")
            ediblesNode.child("comments").setValue(comments)
            ediblesNode.child("amount").setValue(amount)

            Log.d("Categories", "User input saved in Categories successfully")
        } else {
            throw Exception("User ID is null")
        }
    }

    private fun saveUserInput(comments: String, amount: String) {
        try {
            saveUserInputInCategories(comments, amount) // Save user input in Categories page
            // Save user input in Edibles page
            val userId = auth.currentUser?.uid
            if (userId != null) {
                val userNode = database.reference.child("users").child(userId).child("edibles")
                userNode.child("comments").setValue(comments)
                userNode.child("amount").setValue(amount)

                Log.d("Edibles", "User input saved in Edibles successfully")
            } else {
                throw Exception("User ID is null")
            }

            finish()
        } catch (e: Exception) {
            Log.e("Edibles", "Error saving user input: ${e.message}")
        }
    }

}