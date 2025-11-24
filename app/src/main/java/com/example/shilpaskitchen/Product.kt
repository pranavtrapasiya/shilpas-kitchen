package com.example.shilpaskitchen

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val imageRes: Int,
    val productDetail: String,
    var quantity: Int  ,

)
