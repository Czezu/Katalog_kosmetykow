package com.example.projekt_czezowska_dominika_kosmetyki

import java.io.Serializable
data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val category: String,
    val isPromo: Boolean,
    val description: String,
    val imageResId: Int
) : Serializable