package com.example.shilpaskitchen

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Special : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_special)
        var specialBack = findViewById<ImageView>(R.id.specialbackbtn)
        specialBack.setOnClickListener {
            finish()
        }
    }
}