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
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class Other : AppCompatActivity() {

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_IMAGE_SELECT = 2
    }

    private lateinit var imageButtonOther: ImageButton
    private lateinit var editTextAmountOther: EditText
    private lateinit var editTextCommentsOther: EditText
    private lateinit var btnSubmitOther: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var imageUri: Uri? = null

    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                val selectedImageUri: Uri? = data.data
                if (selectedImageUri != null) {
                    try {
                        imageUri = selectedImageUri
                        imageButtonOther.setImageURI(imageUri)
                    } catch (e: Exception) {
                        Log.e("Other", "Error selecting image: ${e.message}")
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other)


        // Initialize Firebase components
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()


        imageButtonOther = findViewById(R.id.imageButtonOther)
        editTextAmountOther = findViewById(R.id.editTextAmountOther)
        editTextCommentsOther = findViewById(R.id.editTextCommentsOther)
        btnSubmitOther = findViewById(R.id.btnSubmitOther)

        imageButtonOther.setOnClickListener {
            showImageSelectionOptions()
        }

        // Set click listener for submit button
        btnSubmitOther.setOnClickListener {
            // Get the entered data
            val amount: String = editTextAmountOther.text.toString()
            val comments: String = editTextCommentsOther.text.toString()

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
            val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val selectIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

            val chooserIntent = Intent(Intent.ACTION_CHOOSER)
            chooserIntent.putExtra(Intent.EXTRA_INTENT, captureIntent)
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Select Image Source")

            val mimeTypes = arrayOf("image/jpeg", "image/png")
            chooserIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)

            val intentArray = arrayOf(selectIntent)
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)

            if (chooserIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(chooserIntent, REQUEST_IMAGE_CAPTURE)
            } else {
                Log.e("Other", "No activity found to handle image selection")
            }
        }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap?
                    imageButtonOther.setImageBitmap(imageBitmap)
                    imageUri = null // Reset the image URI since it's a captured image
                }
                REQUEST_IMAGE_SELECT -> {
                    val selectedImageUri: Uri? = data?.data
                    if (selectedImageUri != null) {
                        try {
                            imageUri = selectedImageUri
                            imageButtonOther.setImageURI(imageUri)
                        } catch (e: Exception) {
                            Log.e("Other", "Error selecting image: ${e.message}")
                        }
                    }
                }
            }
        }
    }
    private fun saveUserInputInCategories(amount: String, comments: String) {
        val userId = auth.currentUser?.uid

        if (userId != null) {
            val userNode = database.reference.child("users").child(userId).child("categories").child("other")
            userNode.child("amount").setValue(amount)
            userNode.child("comments").setValue(comments)

            // Upload the image to Firebase Storage if an image is selected
            imageUri?.let { uri ->
                val imageName = UUID.randomUUID().toString() + ".jpg"
                val storageRef = FirebaseStorage.getInstance().reference.child("images").child(imageName)
                storageRef.putFile(uri)
                    .addOnSuccessListener { taskSnapshot ->
                        storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                            userNode.child("image").setValue(downloadUri.toString())
                            Log.d("Other", "Image uploaded and saved successfully")
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("Other", "Error uploading image: ${e.message}")
                    }
            }

            Log.d("Other", "User input saved successfully")
        } else {
            throw Exception("User ID is null")
        }
    }





    private fun saveUserInput(amount: String, comments: String) {
        val userId = auth.currentUser?.uid

        if (userId != null) {
            val userNode = database.reference.child("users").child(userId).child("cbd")
            userNode.child("amount").setValue(amount)
            userNode.child("comments").setValue(comments)

            Log.d("Other", "User input saved successfully")
        } else {
            throw Exception("User ID is null")
        }
    }

}