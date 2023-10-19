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

class THC : AppCompatActivity() {

    private lateinit var editTextCommentsTHC: EditText
    private lateinit var editTextAmountTHC: EditText


    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_IMAGE_PICK = 2
    }

    private lateinit var imageButtonTHC: ImageButton
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
                    imageButtonTHC.setImageBitmap(imageBitmap)
                    this.imageUri = imageUri
                } catch (e: IOException) {
                    Log.e("THC", "Error loading image: ${e.message}")
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thc2)

        // Initialize Firebase components
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()


        val editTextCommentsTHC: EditText = findViewById(R.id.editTextCommentsTHC)
        val editTextAmountTHC: EditText = findViewById(R.id.editTextAmountTHC)
        val btnSubmitTHC: Button = findViewById(R.id.btnSubmitTHC)

        imageButtonTHC = findViewById(R.id.imageButtonTHC)

        imageButtonTHC.setOnClickListener {
            showImageSelectionOptions()
        }

        btnSubmitTHC.setOnClickListener {
            val comments: String = editTextCommentsTHC.text.toString()
            val amount: String = editTextAmountTHC.text.toString()

            try {
                saveUserInput(comments, amount)
                navigateToCategories()
            } catch (e: Exception) {
                Log.e("THC", "Error saving user input: ${e.message}")
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
            Log.e("THC", "No activity found to handle image selection")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap?
                    imageButtonTHC.setImageBitmap(imageBitmap)
                    imageUri = null // Reset the image URI since it's a captured image
                }
                REQUEST_IMAGE_PICK -> {
                    val selectedImageUri: Uri? = data?.data
                    if (selectedImageUri != null) {
                        try {
                            val imageBitmap =
                                MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
                            imageButtonTHC.setImageBitmap(imageBitmap)
                            imageUri = selectedImageUri // Store the selected image URI
                        } catch (e: IOException) {
                            Log.e("THC", "Error loading image: ${e.message}")
                        }
                    }
                }
            }
            // Save user input in Categories page
            val comments: String = editTextCommentsTHC.text.toString()
            val amount: String = editTextAmountTHC.text.toString()
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

            val thcNode = userNode.child("thc")
            thcNode.child("comments").setValue(comments)
            thcNode.child("amount").setValue(amount)

            Log.d("Categories", "User input saved in Categories successfully")
        } else {
            throw Exception("User ID is null")
        }
    }


    private fun saveUserInput(comments: String, amount: String) {
        try {
            saveUserInputInCategories(comments, amount) // Save user input in Categories page
            // Save user input in THC page
            val userId = auth.currentUser?.uid
            if (userId != null) {
                val userNode = database.reference.child("users").child(userId).child("thc")
                userNode.child("comments").setValue(comments)
                userNode.child("amount").setValue(amount)

                Log.d("THC", "User input saved in THC successfully")
            } else {
                throw Exception("User ID is null")
            }

            finish()
        } catch (e: Exception) {
            Log.e("THC", "Error saving user input: ${e.message}")
        }
    }

}