package com.example.shilpaskitchen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout

class FinalActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: myAdapter
    private val productList = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final)

        setupToolbar()
        setupNavigationButtons()
        setupRecyclerView()
        populateProductList()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val collapsingToolbar = findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbar)
        val appBarLayout = findViewById<AppBarLayout>(R.id.appBar)
        val logoImage = findViewById<ImageView>(R.id.logo)
        val hamburger = findViewById<ImageView>(R.id.imageView)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        collapsingToolbar.title = ""

        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { layout, verticalOffset ->
            val totalScrollRange = layout.totalScrollRange
            val absOffset = kotlin.math.abs(verticalOffset)
            if (absOffset >= totalScrollRange) {
                logoImage.alpha = 0f
                collapsingToolbar.title = "Shilpa's Kitchen"
            } else {
                collapsingToolbar.title = ""
                logoImage.alpha = 1.0f - (absOffset.toFloat() / totalScrollRange)
            }
        })

        // Popup menu on hamburger icon
        hamburger.setOnClickListener { view ->
            androidx.appcompat.widget.PopupMenu(this, view).apply {
                menuInflater.inflate(R.menu.main_menu, menu)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.menu_login -> {
                            startActivity(Intent(this@FinalActivity, LoginActivity::class.java))
                            true
                        }
                        R.id.menu_signup -> {
                            startActivity(Intent(this@FinalActivity, SignupActivity::class.java))
                            true
                        }
                        R.id.menu_cart -> {
                            startActivity(Intent(this@FinalActivity, CartActivity::class.java))
                            true
                        }
                        R.id.menu_track -> {
                            startActivity(Intent(this@FinalActivity, OrdersActivity::class.java))
                            true
                        }
                        
                        R.id.menu_admin -> {
                            promptAdminAccess()
                            true
                        }
                        else -> false
                    }
                }
                show()
            }
        }
    }

    private fun promptAdminAccess() {
        val input = android.widget.EditText(this)
        input.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Admin Access")
            .setMessage("Enter admin PIN")
            .setView(input)
            .setPositiveButton("OK") { dialog, _ ->
                val pin = input.text.toString()
                if (pin == "1234") {
                    startActivity(Intent(this, AdminActivity::class.java))
                } else {
                    android.widget.Toast.makeText(this, "Invalid PIN", android.widget.Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun setupNavigationButtons() {
        findViewById<ImageView>(R.id.homeIcon).setOnClickListener {
            recreate()
        }

        findViewById<ImageView>(R.id.whatsappIcon).setOnClickListener {
            val phone = "+919377732558"
            val message = "Hello! I want to order something!"
            val url = "https://wa.me/${phone.replace("+", "")}?text=${Uri.encode(message)}"
            startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
        }

        findViewById<ImageView>(R.id.cartIcon).setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.userProfileIcon).setOnClickListener {
            startActivity(Intent(this, OrdersActivity::class.java))
        }

        findViewById<TextView>(R.id.trendy).setOnClickListener {
            startActivity(Intent(this, Trendy::class.java))
        }

        findViewById<TextView>(R.id.special).setOnClickListener {
            startActivity(Intent(this, Special::class.java))
        }

        findViewById<TextView>(R.id.other).setOnClickListener {
            startActivity(Intent(this, Other::class.java))
        }
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        adapter = myAdapter(
            productList = productList,
            onAction = { product ->
                CartManager.addToCart(product)
                Toast.makeText(this, "${product.name} added to cart", Toast.LENGTH_SHORT).show()
            },
            onCartUpdated = null,
            onItemClick = { product ->
                val intent = Intent(this, detail_product::class.java).apply {
                    // Use keys expected by detail_product
                    putExtra("id", product.id)
                    putExtra("name", product.name)
                    putExtra("price", product.price)
                    putExtra("image", product.imageRes)
                    putExtra("detail", product.productDetail)
                    putExtra("quantity", product.quantity.toString())
                }
                startActivity(intent)
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun populateProductList() {
        productList.apply {
            clear()
            add(Product(1, "Thepla", 150.0, R.drawable.thepla,
                "Enjoy the authentic taste of Gujarat with our soft and spicy methi theplas. Prepared using fresh fenugreek leaves, wheat flour, and a special blend of traditional spices, our theplas are perfect for breakfast, travel, or as a snack with chutney or curd. They're soft, nutritious, and made with homemade love – just like your nani's kitchen.",500))
            add(Product(2, "Chakri", 250.0, R.drawable.chakri,
                "Our crispy Chakris are made from a rich mix of rice flour, sesame seeds, and ajwain, twisted into beautiful spirals and deep-fried till golden brown. Each bite is a burst of crunchy texture and subtle spice – perfect with a cup of hot tea or for serving during festive occasions like Diwali and family gatherings.", 500))
            add(Product(3, "Farsi Puri", 200.0, R.drawable.farsi_puri,
                "Farsi Puri is a beloved Gujarati snack known for its crispy and flaky texture. Made using refined flour, black pepper, and ghee, each puri is rolled and fried to perfection. These puris have a rich, savory flavor and a shelf life that makes them ideal for travel or festivals. Great as a tea-time companion or with achaar.", 500))
            add(Product(4, "Gathiya", 200.0, R.drawable.gathiya,
                "Gathiya is a soft, mildly spiced gram flour snack that melts in your mouth. Unlike crunchy sev, this version is thicker, airy, and softer. Popular in every Gujarati household, it is often paired with hot masala chai or raw papaya chutney. Our version is made fresh with besan and a dash of carom seeds for that authentic flavor.", 500))
            add(Product(5, "Makai Pauva", 200.0, R.drawable.makai_pauva,
                "Makai Pauva, also known as corn flakes chivda, is a light, crunchy snack made from roasted maize flakes tossed with peanuts, curry leaves, mustard seeds, and mild spices. It’s a perfect munchie when you want something light yet satisfying. We make it fresh in small batches to retain its crunch and natural sweetness of corn.", 500))
            add(Product(6, "Methi Para", 200.0, R.drawable.methi_para,
                "Our Methi Para is a golden-fried snack made from wheat flour and fresh fenugreek leaves, seasoned with carom seeds and spices. Each bite offers a satisfying crunch and earthy bitterness from the methi – a snack that brings both nostalgia and comfort. Ideal for tea-time or gifting during festivities. Freshly made and stored hygienically for long-lasting crispiness.", 500))
            add(Product(7, "Sakkar Para", 200.0, R.drawable.sakkar_para,
                "Sakkar Para is a delightful Gujarati sweet snack made by deep frying diamond-shaped flour dough and coating them in sugar syrup. Crisp on the outside and sweet all the way through, these golden bites are perfect for festive treats or a sweet craving at any time. Homemade style with no preservatives, just like mom makes them!", 500))
            add(Product(8, "Sev Mamra", 200.0, R.drawable.sev_mamra,
                "Sev Mamra is a light, crunchy mixture...", 500))
            add(Product(9, "Sev", 200.0, R.drawable.sev,
                "Our Sev is thin, crispy, and perfectly spiced...", 500))
        }
        adapter.notifyDataSetChanged()
    }
}
