package com.example.opsc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class viewOther : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var submissionsRef: DatabaseReference
    private lateinit var categoryTextView: TextView
    private lateinit var amountTextView: TextView
    private lateinit var commentsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_other)
        categoryTextView = findViewById(R.id.categoryTextView)
        amountTextView = findViewById(R.id.amountTextView)
        commentsTextView = findViewById(R.id.commentsTextView)

        // Initialize Firebase components
        database = FirebaseDatabase.getInstance()
        // Retrieve the user ID from the intent
        val userId = intent.getStringExtra("userId")
        if (userId != null) {
            submissionsRef = database.reference.child("users").child(userId)
                .child("categories").child("submissions").child("Other")
        }

        // Read the Other submission data from Firebase Realtime Database
        readOtherSubmissionFromDatabase()

    }
    private fun readOtherSubmissionFromDatabase() {
        val otherSubmissionListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val category = dataSnapshot.child("category").value.toString()
                    val amount = dataSnapshot.child("amount").value.toString()
                    val comments = dataSnapshot.child("comments").value.toString()

                    categoryTextView.text = category
                    amountTextView.text = amount
                    commentsTextView.text = comments
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors that occurred while reading the data
            }
        }

        submissionsRef.addValueEventListener(otherSubmissionListener)
    }

}