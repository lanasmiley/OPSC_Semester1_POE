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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.database.FirebaseDatabase


class LoginActivity : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance()
    private val db = database.getReference("users")
    private lateinit var auth: FirebaseAuth
    private lateinit var btn_login: Button
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var forgotPasswordLink: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        forgotPasswordLink = findViewById(R.id.forgotPasswordLink)
        btn_login = findViewById(R.id.btn_login)


        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()



        btn_login.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (isValidCredentials(email, password)) {
                signInWithEmailAndPassword(email, password)
            } else {
                Log.d("Login", "Invalid Credentials")
                // Display an error message or take appropriate action
            }
        }

        forgotPasswordLink.setOnClickListener {
            val email = editTextEmail.text.toString()
            if (email.isNotEmpty()) {
                resetPassword(email)
            } else {
                Log.d("Login", "Forgot Password: Invalid email")
                // Display an error message or take appropriate action
            }
        }
    }

    private fun isValidCredentials(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Re-instantiate the instance for each user that logs in
                    db.child(auth.currentUser?.uid ?: "").setValue(true)
                    navigateToMainActivity()
                    Log.d("Login", "Successful Login")
                } else {
                    handleLoginFailure(task.exception)
                }
            }
    }

    private fun handleLoginFailure(exception: Exception?) {
        when (exception) {
            is FirebaseAuthInvalidUserException -> {
                Log.d("Login", "Invalid user: ${exception.message}")
                // Display an error message for invalid user
            }
            is FirebaseAuthInvalidCredentialsException -> {
                Log.d("Login", "Invalid credentials: ${exception.message}")
                // Display an error message for invalid credentials
            }
            else -> {
                Log.d("Login", "Login failed: ${exception?.message}")
                // Display a generic error message or handle the failure
            }
        }
    }

    private fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Login", "Password reset email sent successfully")
                    // Show a success message to the user
                } else {
                    Log.d(
                        "Login",
                        "Failed to send password reset email: ${task.exception?.message}"
                    )
                    // Show an error message to the user or handle the failure
                }
            }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, AddCategory::class.java)
        startActivity(intent)
        finish() // Optional: Finish LoginActivity so it
    }
}

