package com.example.opsc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar

class Achievements : AppCompatActivity() {

    private lateinit var progressCBD: ProgressBar
    private lateinit var progressTHC: ProgressBar
    private lateinit var progressEdibles: ProgressBar
    private lateinit var progressOther: ProgressBar
    private var currentProgressCBD = 0
    private var currentProgressTHC = 0
    private var currentProgressEdibles = 0
    private var currentProgressOther = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievements2)

        progressCBD = findViewById(R.id.progressCBD)
        progressTHC = findViewById(R.id.progressTHC)
        progressEdibles = findViewById(R.id.progressEdibles)
        progressOther = findViewById(R.id.progressOther)

    }
}