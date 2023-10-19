package com.example.opsc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class viewTHC : AppCompatActivity() {

    private lateinit var categoryTextView: TextView
    private lateinit var amountTextView: TextView
    private lateinit var commentsTextView: TextView

    private lateinit var database: FirebaseDatabase
    private lateinit var submissionRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_thc)

        // Initialize Firebase components
        database = FirebaseDatabase.getInstance()
        val userId = intent.getStringExtra("userId")
        if (userId != null) {
            submissionRef = database.reference.child("users").child(userId)
                .child("categories").child("submissions").child("THC")
        }

        // Initialize the TextViews
        categoryTextView = findViewById(R.id.categoryTextView)
        amountTextView = findViewById(R.id.amountTextView)
        commentsTextView = findViewById(R.id.commentsTextView)

        // Read submission from Firebase Realtime Database
        readSubmissionFromDatabase()
    }

    private fun readSubmissionFromDatabase() {
        val submissionListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val category = dataSnapshot.child("category").value.toString()
                val amount = dataSnapshot.child("amount").value.toString()
                val comments = dataSnapshot.child("comments").value.toString()

                // Display the retrieved data
                categoryTextView.text = "Category: $category"
                amountTextView.text = "Amount: $amount"
                commentsTextView.text = "Comments: $comments"
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("viewTHC", "Error reading submission: ${databaseError.message}")
            }
        }

        // Attach the listener to the submissionRef
        submissionRef.addValueEventListener(submissionListener)
    }
}

