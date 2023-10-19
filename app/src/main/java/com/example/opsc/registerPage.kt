package com.example.opsc


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class registerPage : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signInButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page)

        auth = FirebaseAuth.getInstance()

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        signInButton = findViewById(R.id.signInButton)

        // Find the signInLink view
        val signInLink = findViewById<TextView>(R.id.signInLink)

        // Set a click listener on the signInLink view
        signInLink.setOnClickListener {
            // Start the sign-in activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


        signInButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signUp(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    // User registration successful, perform any necessary actions

                    // For example, you can navigate to the sign-in page
                    Log.d("RegisterActivity", "Navigating to signInPage activity")
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    // User registration failed, handle the error
                    val errorMessage = task.exception?.message
                    Log.d("RegisterActivity", "Sign up failed: $errorMessage")
                    Toast.makeText(this, "Sign up failed: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }
    }
}