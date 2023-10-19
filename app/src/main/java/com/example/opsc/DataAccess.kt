package com.example.opsc

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DataAccess {
    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("users")

    fun authenticateUser(email: String, password: String, callback: (Boolean) -> Unit) {
        usersRef.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var isAuthenticated = false

                    for (userSnapshot in dataSnapshot.children) {
                        val user = userSnapshot.getValue(User::class.java)
                        if (user?.password == password) {
                            isAuthenticated = true
                            break
                        }
                    }

                    callback(isAuthenticated)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                    callback(false)
                }
            })
    }

    // Other methods and operations
    // ...

}
data class User(
    val email: String = "",
    val password: String = "",
    // Other user fields
)
