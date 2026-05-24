package com.example.projekt_czezowska_dominika_kosmetyki

import android.content.Context
import java.io.File

object FavoritesManager {
    private const val FILE_NAME = "katalog_kosmetykow_favs.txt"
    private val favoriteIds = mutableSetOf<Int>()

    fun toggleFavorite(context: Context, productId: Int, isFav: Boolean) {
        if (isFav) {
            favoriteIds.add(productId)
        } else {
            favoriteIds.remove(productId)
        }
        ProductRepository.getProductById(productId)?.isFavorite = isFav
        saveToFile(context)
    }

    fun isFavorite(productId: Int): Boolean = favoriteIds.contains(productId)

    fun getFavoriteProducts(): List<Product> {
        return ProductRepository.list.filter { favoriteIds.contains(it.id) }
    }

    fun saveToFile(context: Context) {
        try {
            val file = File(context.filesDir, FILE_NAME)
            file.writeText(favoriteIds.joinToString(","))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadFromFile(context: Context) {
        try {
            val file = File(context.filesDir, FILE_NAME)
            favoriteIds.clear()
            if (file.exists()) {
                val content = file.readText()
                if (content.isNotEmpty()) {
                    val ids = content.split(",").mapNotNull { it.toIntOrNull() }
                    favoriteIds.addAll(ids)
                    for (prod in ProductRepository.list) {
                        prod.isFavorite = favoriteIds.contains(prod.id)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}