package com.example.shilpaskitchen

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shilpaskitchen.data.AppDatabase
import com.example.shilpaskitchen.data.Order
import kotlinx.coroutines.launch

class OrdersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)
        val orderBack = findViewById<ImageView>(R.id.orderbackbtn)
        orderBack.setOnClickListener { finish() }

        val recycler = RecyclerView(this)
        setContentView(recycler)
        recycler.layoutManager = LinearLayoutManager(this)

        val db = AppDatabase.getInstance(this)
        val orderDao = db.orderDao()
        val email = getSharedPreferences("auth", MODE_PRIVATE).getString("email", "guest@local") ?: "guest@local"

        lifecycleScope.launch {
            val orders: List<Order> = orderDao.getOrdersForUser(email)
            recycler.adapter = AdminOrdersAdapter(orders, orderDao)
        }
    }
}