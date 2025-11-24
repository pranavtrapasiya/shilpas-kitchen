package com.example.shilpaskitchen

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class detail_product : AppCompatActivity() {

    private lateinit var productImageView: ImageView
    private lateinit var nameView: TextView
    private lateinit var priceView: TextView
    private lateinit var quantityView: TextView
    private lateinit var detailView: TextView
    private lateinit var addToCartBtn: Button
    private lateinit var backBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)

        initializeViews()
        populateFromIntent()
        setupClickListeners()
    }

    private fun initializeViews() {
        productImageView = findViewById(R.id.productImage)
        nameView = findViewById(R.id.productName)
        priceView = findViewById(R.id.productPrice)
        quantityView = findViewById(R.id.productquantity)
        detailView = findViewById(R.id.productDetail)
        addToCartBtn = findViewById(R.id.addToCartBtn)
        backBtn = findViewById(R.id.backbtn2)
    }

    private fun populateFromIntent() {
        val id = intent.getIntExtra("id", 0)
        val name = intent.getStringExtra("name") ?: ""
        val price = intent.getDoubleExtra("price", 0.0)
        val quantityDisplay = intent.getStringExtra("quantity") ?: "1"
        val detail = intent.getStringExtra("detail") ?: ""
        val imageRes = intent.getIntExtra("image", R.drawable.thepla)

        nameView.text = name
        priceView.text = "Price: ₹$price"
        quantityView.text = "Quantity: $quantityDisplay"
        detailView.text = detail
        productImageView.setImageResource(imageRes)

        addToCartBtn.setOnClickListener {
            val product = Product(
                id = id,
                name = name,
                price = price,
                imageRes = imageRes,
                productDetail = detail,
                quantity = quantityDisplay.toIntOrNull() ?: 1
            )
            CartManager.addToCart(product)
        }
    }

    private fun setupClickListeners() {
        backBtn.setOnClickListener { finish() }
    }
}
