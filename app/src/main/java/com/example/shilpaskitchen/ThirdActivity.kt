package com.example.shilpaskitchen

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.shilpaskitchen.R

class ThirdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.third_page)

        val finishbtn = findViewById<Button>(R.id.finishbtn)
        finishbtn.setOnClickListener {
            startActivity(Intent(this, FinalActivity::class.java))

        }
    }
}
