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
import android.widget.ProgressBar
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CBD : AppCompatActivity() {

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_IMAGE_PICK = 2
    }
    private lateinit var progressBarCBD: ProgressBar
    private var currentProgress = 0


    private lateinit var imageButtonCBD: ImageButton
    private lateinit var editTextAmountCBD: EditText
    private lateinit var editTextCommentsCBD: EditText
    private lateinit var btnSubmitCBD: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var imageUri: Uri? = null

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            imageButtonCBD.setImageBitmap(imageBitmap)
        }
    }


    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.data?.let { imageUri ->
                val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                imageButtonCBD.setImageBitmap(imageBitmap)
            }
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cbd)


        // Initialize Firebase components
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        progressBarCBD = findViewById(R.id.progressCBD)

        imageButtonCBD = findViewById(R.id.imageButtonCBD)
        editTextAmountCBD = findViewById(R.id.editTextAmountCBD)
        editTextCommentsCBD = findViewById(R.id.editTextCommentsCBD)
        btnSubmitCBD = findViewById(R.id.btnSubmitCBD)

        imageButtonCBD.setOnClickListener {
            showImageSelectionOptions()
        }

        btnSubmitCBD.setOnClickListener {
            val amount: String = editTextAmountCBD.text.toString()
            val comments: String = editTextCommentsCBD.text.toString()

            try {
                saveUserInput(comments, amount)
                updateProgressBar() // Add this line to update the progress bar
                navigateToCategories()
            } catch (e: Exception) {
                Log.e("Categories", "Error saving user input: ${e.message}")
            }
        }
    }
    private fun updateProgressBar() {
        currentProgress += 10 // Increase the progress by a certain value
        progressBarCBD.progress = currentProgress // Update the progress bar
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
            selectImageLauncher.launch(chooserIntent)
        } else {
            Log.e("CBD", "No activity found to handle image selection")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap?
                    imageBitmap?.let {
                        // Process the captured image bitmap here
                        imageButtonCBD.setImageBitmap(imageBitmap)
                    }
                }
                REQUEST_IMAGE_PICK -> {
                    val imageUri = data?.data
                    imageUri?.let {
                        // Process the selected image URI here
                        val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                        imageButtonCBD.setImageBitmap(imageBitmap)
                    }
                }
            }
        }
    }


    private fun saveUserInput(amount: String, comments: String) {
        val userId = auth.currentUser?.uid

        if (userId != null) {
            val userNode = database.reference.child("users").child(userId).child("cbd")
            userNode.child("amount").setValue(amount)
            userNode.child("comments").setValue(comments)

            Log.d("CBD", "User input saved successfully")
        } else {
            throw Exception("User ID is null")
        }
    }

}