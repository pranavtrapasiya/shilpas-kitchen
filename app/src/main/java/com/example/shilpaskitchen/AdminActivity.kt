package com.example.shilpaskitchen

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.RecyclerView
import com.example.shilpaskitchen.data.AppDatabase
import com.example.shilpaskitchen.data.Order
import com.example.shilpaskitchen.data.OrderDao
import com.example.shilpaskitchen.data.User

class AdminActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val db = AppDatabase.getInstance(this)
        val userDao = db.userDao()
        val orderDao = db.orderDao()

        lifecycleScope.launch {
            val users = mutableListOf<User>()
            val orders = mutableListOf<Order>()
            // Fetch all orders; for simplicity, you can extend with a users list view later
            try {
                orders.addAll(orderDao.getAllOrders())
                recyclerView.adapter = AdminOrdersAdapter(orders, orderDao)
            } catch (e: Exception) {
                Toast.makeText(this@AdminActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


