package com.example.projekt_czezowska_dominika_kosmetyki

import android.content.Context
import java.io.File
import com.example.projekt_czezowska_dominika_kosmetyki.ProductRepository
object CartManager {
    private const val FILE_NAME = "katalog_kosmetykow.txt"
    val cartItems = mutableListOf<CartItem>()

    fun addToCart(context: Context, productId: Int, quantity: Int = 1) {
        val product = ProductRepository.getProductById(productId)
        val size = product?.specification ?: "Brak danych"
        val existing = cartItems.find { it.productId == productId && it.selectedSize == size }

        if (existing != null) {
            existing.quantity += quantity
        } else {
            cartItems.add(CartItem(productId, quantity, size))
        }

        saveToFile(context)
    }

    fun updateQuantity(context: Context, productId: Int, size: String, delta: Int) {
        val item = cartItems.find { it.productId == productId && it.selectedSize == size }
        if (item != null) {
            item.quantity += delta
            if (item.quantity <= 0) cartItems.remove(item)
            saveToFile(context)
        }
    }

    fun clearCart(context: Context) {
        cartItems.clear()
        saveToFile(context)
    }

    fun getTotalPrice(): Double {
        return cartItems.sumOf { item ->
            val product = ProductRepository.getProductById(item.productId)
            (product?.price ?: 0.0) * item.quantity
        }
    }

    fun saveToFile(context: Context) {
        val file = File(context.filesDir, FILE_NAME)
        val dataString = cartItems.joinToString(";") { "${it.productId},${it.quantity},${it.selectedSize}" }
        file.writeText(dataString)
    }

    fun loadFromFile(context: Context) {
        val file = File(context.filesDir, FILE_NAME)
        cartItems.clear()
        if (!file.exists()) return
        val contents = file.readText()
        contents.split(";").forEach { pair ->
            val parts = pair.split(",")
            if (parts.size == 3) {
                val pid = parts[0].toIntOrNull()
                val qty = parts[1].toIntOrNull()
                val size = parts[2]
                if (pid != null && qty != null) cartItems.add(CartItem(pid, qty, size))
            }
        }
    }
}