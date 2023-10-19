package com.example.opsc


import com.example.opsc.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class Categories : AppCompatActivity() {

    private lateinit var btnCBD: Button
    private lateinit var btnTHC: Button
    private lateinit var btnEdibles: Button
    private lateinit var btnOther: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.categories)

        /*btnCBD = findViewById(R.id.btnCBD)
        tvCBDTitle = findViewById(R.id.tvCBDTitle)
        btnTHC = findViewById(R.id.btnTHC)
        tvTHCTitle = findViewById(R.id.tvTHCTitle)
        btnEdibles = findViewById(R.id.btnEdibles)
        tvEdiblesTitle = findViewById(R.id.tvEdiblesTitle)
        btnOther = findViewById(R.id.btnOther)
        tvOtherTitle = findViewById(R.id.tvOtherTitle)*/

        btnCBD = findViewById(R.id.btnCBD)
        btnTHC = findViewById(R.id.btnTHC)
        btnEdibles = findViewById(R.id.btnEdibles)
        btnOther = findViewById(R.id.btnOther)

        btnCBD.setOnClickListener {
            val intent = Intent(this@Categories, viewCBD::class.java)

            // Pass the necessary data to the viewCBD activity
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                intent.putExtra("userId", userId)
            }

            startActivity(intent)
        }



        btnTHC.setOnClickListener {
            val intent = Intent(this@Categories, viewTHC::class.java)

            // Pass the necessary data to the viewTHC activity
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                intent.putExtra("userId", userId)
            }

            startActivity(intent)
        }


        btnEdibles.setOnClickListener {
            val intent = Intent(this@Categories, viewEdibles::class.java)

            val userID = FirebaseAuth.getInstance().currentUser?.uid
            if (userID != null){
                intent.putExtra("userId", userID)
            }
            startActivity(intent)
        }

        btnOther.setOnClickListener {
            val intent = Intent(this@Categories, viewOther::class.java)
            val userID = FirebaseAuth.getInstance().currentUser?.uid
            if (userID != null){
                intent.putExtra("userId", userID)
            }
            startActivity(intent)
        }

        val buttonNext2: Button = findViewById(R.id.buttonNext2)

        buttonNext2.setOnClickListener {
            navigateToCategory()
        }
    }

    private fun navigateToCategory() {
        val intent = Intent(this, Achievements::class.java)
        startActivity(intent)
    }
}