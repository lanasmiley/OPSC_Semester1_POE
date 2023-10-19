package com.example.opsc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class signInPage : AppCompatActivity() {

    private lateinit var btnLogin: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_page)
        auth = FirebaseAuth.getInstance()
        // Initialize views
        btnLogin = findViewById<Button>(R.id.btn_login)

        // Set click listener for login button
        btnLogin.setOnClickListener {
            // Perform login logic here
            val email = "user@example.com" // Replace with the user's input from EditText
            val password = "password123" // Replace with the user's input from EditText

            // Call the signInWithEmailAndPassword method to authenticate the user
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Login successful, start the Add Category activity
                        val intent = Intent(this, AddCategory::class.java)
                        startActivity(intent)
                    } else {
                        // Login failed, display an error message
                        Toast.makeText(
                            this,
                            "Incorrect password or invalid user",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if a user is already signed in
        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser != null) {
            // User is already signed in, start the Add Category activity
            val intent = Intent(this, AddCategory::class.java)
            startActivity(intent)
            finish() // Optional: finish the current activity to prevent going back to the sign-in page
        }
    }
}