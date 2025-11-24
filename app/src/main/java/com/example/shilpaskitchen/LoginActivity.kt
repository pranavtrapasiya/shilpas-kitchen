package com.example.shilpaskitchen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailField = findViewById<EditText>(R.id.email)
        val passwordField = findViewById<EditText>(R.id.password)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val signupBtn = findViewById<Button>(R.id.toSignup)

        loginBtn.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString()

            // Basic validation
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = com.example.shilpaskitchen.data.AppDatabase.getInstance(this)
            val userDao = db.userDao()
            lifecycleScope.launch {
                val user = userDao.getByEmail(email)
                if (user == null) {
                    Toast.makeText(this@LoginActivity, "No account found. Please sign up first.", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                val enteredHash = hashPassword(password)
                if (enteredHash == user.passwordHash) {
                    getSharedPreferences("auth", Context.MODE_PRIVATE)
                        .edit()
                        .putString("email", user.email)
                        .putBoolean("logged_in", true)
                        .apply()
                    Toast.makeText(this@LoginActivity, "Logged in as ${user.email}", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, FinalActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        signupBtn.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun hashPassword(input: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(input.toByteArray())
        return hashBytes.joinToString("") { byte -> "%02x".format(byte) }
    }
}


