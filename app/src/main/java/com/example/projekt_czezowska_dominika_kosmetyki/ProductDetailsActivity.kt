package com.example.projekt_czezowska_dominika_kosmetyki

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ProductDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        val product = intent.getSerializableExtra("PRODUCT_DATA") as? Product
        if (product == null) {
            finish()
            return
        }

        findViewById<TextView>(R.id.detailName).text = product.name
        findViewById<TextView>(R.id.detailPrice).text = String.format("%.2f zł", product.price)
        findViewById<TextView>(R.id.detailDescription).text = product.description
        findViewById<ImageView>(R.id.detailImage).setImageResource(product.imageResId)

        val specLabel = findViewById<TextView>(R.id.detailSpecLabel)
        val specValue = findViewById<TextView>(R.id.detailSpecValue)

        if (product.specification.isBlank()) {
            specLabel.visibility = View.GONE
            specValue.visibility = View.GONE
        } else {
            specLabel.visibility = View.VISIBLE
            specValue.visibility = View.VISIBLE
            specValue.text = product.specification
        }

        val favCheckbox = findViewById<CheckBox>(R.id.detailFavCheckbox)
        favCheckbox.isChecked = FavoritesManager.isFavorite(product.id)
        favCheckbox.setOnCheckedChangeListener { _, isChecked ->
            FavoritesManager.toggleFavorite(this, product.id, isChecked)
        }

        findViewById<Button>(R.id.detailAddToCartBtn).setOnClickListener {
            val variant = if (product.specification.isNotBlank()) product.specification else "Standard"
            CartManager.addToCart(this, product.id, 1)
            Toast.makeText(this, "Dodano do koszyka!", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.detailCloseBtn).setOnClickListener {
            finish()
        }
    }
}