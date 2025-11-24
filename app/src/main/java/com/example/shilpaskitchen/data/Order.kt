package com.example.shilpaskitchen.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userEmail: String,
    val totalAmount: Double,
    val status: String, // e.g., Placed, Processing, Shipped, Delivered
    val createdAt: Long = System.currentTimeMillis()
)




