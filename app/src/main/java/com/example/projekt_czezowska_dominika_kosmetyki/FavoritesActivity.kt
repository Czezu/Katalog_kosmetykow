package com.example.projekt_czezowska_dominika_kosmetyki

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class FavoritesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noFavMsg: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        FavoritesManager.loadFromFile(this)

        recyclerView = findViewById(R.id.favoritesRecyclerView)
        noFavMsg = findViewById(R.id.noFavMsg)

        recyclerView.layoutManager = GridLayoutManager(this, 2)

        setupNav()
        renderFavorites()
    }

    override fun onResume() {
        super.onResume()
        renderFavorites()
    }

    private fun setupNav() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationViewFav)

        bottomNav.selectedItemId = R.id.nav_favorites

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_catalog -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }

                R.id.nav_favorites -> true

                R.id.nav_cart -> {
                    startActivity(Intent(this, CartActivity::class.java))
                    finish()
                    true
                }

                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }

                else -> false
            }
        }
    }

    private fun renderFavorites() {

        val list = FavoritesManager.getFavoriteProducts()

        noFavMsg.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE

        recyclerView.adapter = FavoritesAdapter(list)
    }

    inner class FavoritesAdapter(
        private val favProducts: List<Product>
    ) : RecyclerView.Adapter<FavoritesAdapter.VH>() {

        inner class VH(view: View) : RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_card, parent, false)
            return VH(view)
        }

        override fun getItemCount(): Int = favProducts.size

        override fun onBindViewHolder(holder: VH, position: Int) {

            val product = favProducts[position]
            val view = holder.itemView

            val name = view.findViewById<TextView>(R.id.productName)
            val price = view.findViewById<TextView>(R.id.productPrice)
            val img = view.findViewById<ImageView>(R.id.productImg)
            val badge = view.findViewById<TextView>(R.id.productPromoBadge)
            val fav = view.findViewById<CheckBox>(R.id.favCheckbox)
            val cartBtn = view.findViewById<Button>(R.id.addCartBtnFast)

            name.text = product.name
            price.text = "${product.price} zł"
            img.setImageResource(product.imageResId)

            badge.visibility = if (product.isPromo) View.VISIBLE else View.GONE

            fav.setOnCheckedChangeListener(null)
            fav.isChecked = FavoritesManager.isFavorite(product.id)

            fav.setOnCheckedChangeListener { _, isChecked ->
                FavoritesManager.toggleFavorite(
                    this@FavoritesActivity,
                    product.id,
                    isChecked
                )

                Toast.makeText(
                    this@FavoritesActivity,
                    if (isChecked) "Dodano" else "Usunięto",
                    Toast.LENGTH_SHORT
                ).show()

                renderFavorites()
            }

            cartBtn.setOnClickListener {
                CartManager.addToCart(this@FavoritesActivity, product.id, 1)
                Toast.makeText(
                    this@FavoritesActivity,
                    "Dodano do koszyka",
                    Toast.LENGTH_SHORT
                ).show()
            }

            view.setOnClickListener {
                val intent = Intent(this@FavoritesActivity, ProductDetailsActivity::class.java)
                intent.putExtra("PRODUCT_DATA", product)
                startActivity(intent)
            }
        }
    }
}