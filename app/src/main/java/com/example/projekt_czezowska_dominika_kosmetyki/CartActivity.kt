package com.example.projekt_czezowska_dominika_kosmetyki

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noCartMsg: TextView
    private lateinit var cartTotalText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        CartManager.loadFromFile(this)

        recyclerView = findViewById(R.id.cartRecyclerView)
        noCartMsg = findViewById(R.id.noCartMsg)
        cartTotalText = findViewById(R.id.cartTotal)

        recyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.clearCartBtn).setOnClickListener {
            CartManager.clearCart(this)
            renderCart()
            Toast.makeText(this, "Koszyk wyczyszczony", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.buyNowBtn).setOnClickListener {
            if (CartManager.cartItems.isEmpty()) {
                Toast.makeText(this, "Koszyk jest pusty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            OrderManager.saveOrder(this)
            CartManager.clearCart(this)

            Toast.makeText(this, "Zamówienie złożone", Toast.LENGTH_SHORT).show()
            renderCart()
        }

        findViewById<Button>(R.id.pdfBtn).setOnClickListener {
            PdfManager.generatePdf(this)
            Toast.makeText(this, "Paragon", Toast.LENGTH_SHORT).show()
        }

        setupNav()
        renderCart()
    }

    override fun onResume() {
        super.onResume()
        CartManager.loadFromFile(this)
        renderCart()
    }

    private fun setupNav() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationViewCart)
        bottomNav.selectedItemId = R.id.nav_cart

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_catalog -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_favorites -> {
                    startActivity(Intent(this, FavoritesActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_cart -> true
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun renderCart() {
        val items = CartManager.cartItems

        if (items.isEmpty()) {
            noCartMsg.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            cartTotalText.text = "Razem: 0.00 zł"
            return
        }

        noCartMsg.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE

        recyclerView.adapter = CartAdapter(items)

        cartTotalText.text = String.format(Locale.getDefault(), "Razem: %.2f zł", CartManager.getTotalPrice())
    }

    inner class CartAdapter(
        private val items: List<CartItem>
    ) : RecyclerView.Adapter<CartAdapter.VH>() {

        inner class VH(view: View) : RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_cart_row, parent, false)
            return VH(view)
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: VH, position: Int) {
            val item = items[position]
            val product = ProductRepository.getProductById(item.productId) ?: return
            val view = holder.itemView

            view.findViewById<TextView>(R.id.rowName).text = product.name
            view.findViewById<TextView>(R.id.rowQuantity).text = item.quantity.toString()

            view.findViewById<TextView>(R.id.rowPrice).text =
                String.format(Locale.getDefault(), "%.2f zł", product.price * item.quantity)

            val rowSize = view.findViewById<TextView>(R.id.rowSize)
            if (rowSize != null) {
                rowSize.text = item.selectedSize
            }

            view.findViewById<ImageView>(R.id.rowImg).setImageResource(product.imageResId)

            view.findViewById<View>(R.id.rowBtnAction1).setOnClickListener {
                CartManager.updateQuantity(this@CartActivity, item.productId, item.selectedSize, -1)
                renderCart()
            }

            view.findViewById<View>(R.id.rowBtnAction2).setOnClickListener {
                CartManager.updateQuantity(this@CartActivity, item.productId, item.selectedSize, 1)
                renderCart()
            }

            view.findViewById<View>(R.id.rowRemove).setOnClickListener {
                CartManager.updateQuantity(this@CartActivity, item.productId, item.selectedSize, -999)
                renderCart()
                Toast.makeText(this@CartActivity, "Usunięto z koszyka", Toast.LENGTH_SHORT).show()
            }
        }
    }
}