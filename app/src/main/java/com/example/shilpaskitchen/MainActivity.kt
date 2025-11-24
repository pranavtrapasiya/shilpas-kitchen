package com.example.shilpaskitchen

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nextbtn = findViewById<Button>(R.id.fnbtn)
        nextbtn.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
            finish()
        }
    }
}