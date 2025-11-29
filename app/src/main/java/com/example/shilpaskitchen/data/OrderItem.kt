package com.example.shilpaskitchen.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_items")
data class OrderItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderId: Int,
    val productName: String,
    val quantity: Int,
    val priceEach: Double
)






