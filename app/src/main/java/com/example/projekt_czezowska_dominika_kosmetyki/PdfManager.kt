package com.example.projekt_czezowska_dominika_kosmetyki

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

object PdfManager {

    fun generatePdf(context: Context) {

        try {

            val document = PdfDocument()

            val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
            val page = document.startPage(pageInfo)
            val canvas = page.canvas
            val paint = Paint()

            var y = 40

            // HEADER
            paint.textSize = 18f
            paint.isFakeBoldText = true
            canvas.drawText("PARAGON", 40f, y.toFloat(), paint)

            y += 40

            paint.textSize = 12f
            paint.isFakeBoldText = false

            for (item in CartManager.cartItems) {

                val product = ProductRepository.getProductById(item.productId)

                if (product != null) {

                    val subtotal = product.price * item.quantity

                    canvas.drawText(product.name, 20f, y.toFloat(), paint)
                    y += 20

                    canvas.drawText("${item.quantity} x ${item.selectedSize}", 20f, y.toFloat(), paint)
                    y += 20

                    canvas.drawText("%.2f zł".format(subtotal), 20f, y.toFloat(), paint)
                    y += 30
                }
            }

            y += 20
            paint.isFakeBoldText = true

            canvas.drawText(
                "RAZEM: %.2f zł".format(CartManager.getTotalPrice()),
                20f,
                y.toFloat(),
                paint
            )

            document.finishPage(page)

            val file = File(context.getExternalFilesDir(null), "paragon.pdf")

            document.writeTo(FileOutputStream(file))
            document.close()

            openPdf(context, file)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openPdf(context: Context, file: File) {

        val uri = FileProvider.getUriForFile(
            context,
            context.packageName + ".provider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(intent)
    }
}