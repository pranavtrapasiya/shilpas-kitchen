package com.example.shilpaskitchen

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.security.MessageDigest

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val nameField = findViewById<EditText>(R.id.name)
        val emailField = findViewById<EditText>(R.id.email)
        val passwordField = findViewById<EditText>(R.id.password)
        val confirmPasswordField = findViewById<EditText>(R.id.confirmPassword)
        val passwordStrengthView = findViewById<TextView>(R.id.passwordStrength)
        val signupBtn = findViewById<Button>(R.id.signupBtn)
        val loginBtn = findViewById<Button>(R.id.toLogin)

        signupBtn.setOnClickListener {
            val name = nameField.text.toString().trim()
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString()

            if (name.isEmpty()) {
                Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != confirmPasswordField.text.toString()) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save registration into Room DB
            val passwordHash = hashPassword(password)
            val db = com.example.shilpaskitchen.data.AppDatabase.getInstance(this)
            val userDao = db.userDao()
            // Insert on background thread
            lifecycleScope.launch {
                val existing = userDao.getByEmail(email)
                if (existing != null) {
                    Toast.makeText(this@SignupActivity, "Email already registered", Toast.LENGTH_SHORT).show()
                } else {
                    userDao.insert(com.example.shilpaskitchen.data.User(name = name, email = email, passwordHash = passwordHash))
                    Toast.makeText(this@SignupActivity, "Account created! Please login.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                    finish()
                }
            }
        }

        loginBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Live password strength meter
        passwordField.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                val pwd = s?.toString() ?: ""
                passwordStrengthView.text = passwordStrengthLabel(pwd)
            }
        })
    }

    private fun passwordStrengthLabel(pwd: String): String {
        var score = 0
        if (pwd.length >= 6) score++
        if (pwd.length >= 10) score++
        if (pwd.any { it.isDigit() }) score++
        if (pwd.any { it.isUpperCase() }) score++
        if (pwd.any { !it.isLetterOrDigit() }) score++
        return when (score) {
            0, 1 -> "Weak"
            2, 3 -> "Medium"
            else -> "Strong"
        }
    }

    private fun hashPassword(input: String): String {
        val digest = java.security.MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(input.toByteArray())
        return hashBytes.joinToString("") { byte -> "%02x".format(byte) }
    }
}


