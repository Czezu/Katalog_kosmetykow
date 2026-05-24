package com.example.projekt_czezowska_dominika_kosmetyki

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var productsContainer: RecyclerView
    private lateinit var categorySpinner: Spinner
    private lateinit var promoCheckbox: CheckBox
    private lateinit var searchInput: EditText
    private lateinit var sortRadioGroup: RadioGroup

    private val categories = Category.values()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FavoritesManager.loadFromFile(this)

        productsContainer = findViewById(R.id.productsContainer)
        categorySpinner = findViewById(R.id.categorySpinner)
        promoCheckbox = findViewById(R.id.promoCheckbox)
        searchInput = findViewById(R.id.searchInput)
        sortRadioGroup = findViewById(R.id.sortRadioGroup)

        productsContainer.layoutManager = GridLayoutManager(this, 2)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categories.map { it.displayName }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = renderCatalog()
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        findViewById<Button>(R.id.applyFiltersBtn).setOnClickListener {
            renderCatalog()
        }

        setupBottomNav()
        renderCatalog()
    }

    override fun onResume() {
        super.onResume()
        renderCatalog()
    }

    private fun setupBottomNav() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNav.selectedItemId = R.id.nav_catalog

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_catalog -> true

                R.id.nav_favorites -> {
                    startActivity(Intent(this, FavoritesActivity::class.java))
                    finish()
                    true
                }

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

    private fun renderCatalog() {
        var list = ProductRepository.list

        val selectedCategory = categories[categorySpinner.selectedItemPosition]
        if (selectedCategory != Category.ALL) {
            list = list.filter { it.category == selectedCategory }
        }

        if (promoCheckbox.isChecked) {
            list = list.filter { it.isPromo }
        }

        val query = searchInput.text.toString().trim().lowercase()
        if (query.isNotEmpty()) {
            list = list.filter { it.name.lowercase().contains(query) }
        }

        list = when (sortRadioGroup.checkedRadioButtonId) {
            R.id.radioPriceAsc -> list.sortedBy { it.price }
            R.id.radioPriceDesc -> list.sortedByDescending { it.price }
            R.id.radioNameAsc -> list.sortedBy { it.name }
            else -> list
        }

        findViewById<TextView>(R.id.productCount).text = "${list.size} produktów"
        productsContainer.adapter = ProductAdapter(list)
    }

    inner class ProductAdapter(private val list: List<Product>) :
        RecyclerView.Adapter<ProductAdapter.VH>() {

        inner class VH(view: View) : RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_card, parent, false)
            return VH(view)
        }

        override fun getItemCount() = list.size

        override fun onBindViewHolder(holder: VH, position: Int) {

            val product = list[position]
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
                FavoritesManager.toggleFavorite(this@MainActivity, product.id, isChecked)
                Toast.makeText(
                    this@MainActivity,
                    if (isChecked) "Dodano do ulubionych" else "Usunięto",
                    Toast.LENGTH_SHORT
                ).show()
            }

            cartBtn.setOnClickListener {
                CartManager.addToCart(this@MainActivity, product.id, 1)
                Toast.makeText(
                    this@MainActivity,
                    "Dodano do koszyka",
                    Toast.LENGTH_SHORT
                ).show()
            }

            view.setOnClickListener {
                val intent = Intent(this@MainActivity, ProductDetailsActivity::class.java)
                intent.putExtra("PRODUCT_DATA", product)
                startActivity(intent)
            }
        }
    }
}