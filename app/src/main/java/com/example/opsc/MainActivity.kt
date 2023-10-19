package com.example.opsc
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Perform any initialization or setup here

        // Example: Launch the signUpPage activity
        val intent = Intent(this, registerPage::class.java)
        startActivity(intent)

        // Finish the current activity (optional)
        finish()
    }
}






