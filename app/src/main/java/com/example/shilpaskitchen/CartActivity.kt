package com.example.shilpaskitchen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var videoView: VideoView
    private lateinit var adapter: myAdapter
    private lateinit var cartBack: ImageView
    private lateinit var emptycart: TextView
    private lateinit var totalAmountText: TextView
    private lateinit var checkoutBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView)
        videoView = findViewById(R.id.emptyvid)
        cartBack = findViewById(R.id.cartback)
        emptycart = findViewById(R.id.cartisempty)
        totalAmountText = findViewById(R.id.totalamount)
        checkoutBtn = findViewById(R.id.checkoutBtn)

        // Setup adapter
        adapter = myAdapter(
            productList = CartManager.cartItems.toMutableList(),
            onAction = null,
            onCartUpdated = {
                updateCartDisplay()
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Setup click listeners
        cartBack.setOnClickListener {
            finish()
        }

        checkoutBtn.setOnClickListener {
            val message = buildWhatsAppMessage()
            openWhatsApp(message)
        }

        updateCartDisplay()
    }

    override fun onResume() {
        super.onResume()
        updateCartDisplay()
    }

    private fun updateCartDisplay() {
        val cartItems = CartManager.cartItems

        if (cartItems.isEmpty()) {
            showEmptyCart()
        } else {
            showCartItems()
        }

        val total = CartManager.getTotalPrice()
        totalAmountText.text = "Total: ₹${"%.2f".format(total)}"
    }

    private fun showEmptyCart() {
        recyclerView.visibility = View.GONE
        videoView.visibility = View.VISIBLE
        emptycart.visibility = View.VISIBLE
        totalAmountText.visibility = View.GONE
        checkoutBtn.visibility = View.GONE

        val path = "android.resource://${packageName}/${R.raw.emptycart}"
        videoView.setVideoURI(Uri.parse(path))
        videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
            videoView.start()
        }
    }

    private fun showCartItems() {
        videoView.visibility = View.GONE
        emptycart.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        totalAmountText.visibility = View.VISIBLE
        checkoutBtn.visibility = View.VISIBLE

        adapter.updateProductList(CartManager.cartItems.toMutableList())
    }

    private fun buildWhatsAppMessage(): String {
        val sb = StringBuilder("*New Order*\n\n")
        CartManager.cartItems.forEach { product ->
            val totalPrice = product.price * product.quantity
            sb.append("${product.name} x ${product.quantity} = ₹${"%.2f".format(totalPrice)}\n")
        }
        sb.append("\n*Total: ₹${"%.2f".format(CartManager.getTotalPrice())}*")
        return sb.toString()
    }

    private fun openWhatsApp(message: String) {
        val phone = "+919377732558"
        val url = "https://wa.me/${phone.replace("+", "")}?text=${Uri.encode(message)}"
        val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(url) }
        startActivity(intent)
    }
}
