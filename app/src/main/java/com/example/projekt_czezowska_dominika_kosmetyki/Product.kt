package com.example.projekt_czezowska_dominika_kosmetyki
import java.io.Serializable

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val isPromo: Boolean,
    val category: Category,
    val imageResId: Int,
    val description: String,
    val specification: String,
    var isFavorite: Boolean = false
) : Serializable

data class CartItem(
    val productId: Int,
    var quantity: Int,
    var selectedSize: String
) : Serializable