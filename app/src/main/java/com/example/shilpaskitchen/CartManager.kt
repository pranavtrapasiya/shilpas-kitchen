package com.example.shilpaskitchen

object CartManager {

    val cartItems: MutableList<Product> = mutableListOf()

    fun addToCart(product: Product) {
        val existing = cartItems.find { it.name == product.name }
        if (existing != null) {
            existing.quantity += 1
        } else {
            cartItems.add(product.copy(quantity =1))
        }
    }

    fun removeFromCart(product: Product) {
        cartItems.removeIf { it.name == product.name }
    }

    fun increaseQuantity(product: Product) {
        cartItems.find { it.name == product.name }?.let {
            it.quantity += 1
        }
    }

    fun decreaseQuantity(product: Product) {
        cartItems.find { it.name == product.name }?.let {
            it.quantity -= 1
            if (it.quantity <= 0) {
                removeFromCart(it)
            }
        }
    }

    fun getTotalPrice(): Double {
        return cartItems.sumOf { it.price * it.quantity }
    }

    fun getTotalItems(): Int {
        return cartItems.sumOf { it.quantity }
    }

    fun clearCart() {
        cartItems.clear()
    }

    fun isInCart(product: Product): Boolean {
        return cartItems.any { it.name == product.name }
    }

    fun getCartItem(productName: String): Product? {
        return cartItems.find { it.name == productName }
    }
}
