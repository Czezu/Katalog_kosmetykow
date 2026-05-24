package com.example.projekt_czezowska_dominika_kosmetyki

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object OrderManager {

    fun saveOrder(context: Context) {
        val itemsToSave = ArrayList(CartManager.cartItems)

        if (itemsToSave.isEmpty()) return

        try {
            val formatter = SimpleDateFormat(
                "yyyy_MM_dd_HH_mm_ss",
                Locale.getDefault()
            )
            val currentDate = formatter.format(Date())

            val fileName = "Zamowienie_$currentDate.txt"
            val file = File(context.filesDir, fileName)

            val builder = StringBuilder()
            builder.append("*** KATALOG KOSMETYKÓW ***\n\n")
            builder.append("Data zamówienia: $currentDate\n\n")

            for (item in itemsToSave) {
                val product = ProductRepository.getProductById(item.productId)
                if (product != null) {
                    val subtotal = product.price * item.quantity
                    builder.append("Produkt: ${product.name}\n")
                    builder.append("Rozmiar: ${item.selectedSize}\n")
                    builder.append("Ilość: ${item.quantity}\n")

                    builder.append(String.format(Locale.getDefault(), "Cena: %.2f zł\n", subtotal))
                    builder.append("\n")
                }
            }

            builder.append("----------------------\n")
            builder.append(String.format(Locale.getDefault(), "Razem: %.2f zł", CartManager.getTotalPrice()))

            file.writeText(builder.toString())

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}