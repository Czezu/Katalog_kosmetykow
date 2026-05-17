package com.example.projekt_czezowska_dominika_kosmetyki

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProductDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        val productImageView = findViewById<ImageView>(R.id.productImageView)
        val productNameText = findViewById<TextView>(R.id.productNameText)
        val productPriceText = findViewById<TextView>(R.id.productPriceText)
        val productPromoDetailsBadge = findViewById<TextView>(R.id.productPromoDetailsBadge)
        val productDescriptionText = findViewById<TextView>(R.id.productDescriptionText)
        val favoriteCheckBox = findViewById<CheckBox>(R.id.favoriteCheckBox)
        val addToCartButton = findViewById<Button>(R.id.addToCartButton)
        val backButton = findViewById<Button>(R.id.backButton)

        val produkt = intent.getSerializableExtra("PRODUCT_DATA") as? Product

        if (produkt != null) {
            productNameText.text = produkt.name
            productPriceText.text = "${produkt.price} zł"
            productDescriptionText.text = produkt.description

            productImageView.setImageResource(produkt.imageResId)

            if (produkt.isPromo) {
                productPromoDetailsBadge.visibility = View.VISIBLE
            } else {
                productPromoDetailsBadge.visibility = View.GONE
            }
        }

        addToCartButton.setOnClickListener {
            Toast.makeText(this, "Dodano produkt do koszyka! 🛒", Toast.LENGTH_SHORT).show()
        }

        backButton.setOnClickListener {
            finish()
        }
    }
}