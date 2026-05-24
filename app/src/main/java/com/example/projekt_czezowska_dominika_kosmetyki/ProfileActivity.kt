package com.example.projekt_czezowska_dominika_kosmetyki

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {

    private lateinit var ordersCountText: TextView
    private lateinit var historyContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val nameText = findViewById<TextView>(R.id.profileName)
        val emailText = findViewById<TextView>(R.id.profileEmail)
        ordersCountText = findViewById<TextView>(R.id.profileOrdersCount)
        historyContainer = findViewById<LinearLayout>(R.id.historyContainer)

        nameText.text = "Użytkownik testowy"
        emailText.text = "test@email.com"

        setupBottomNav()
        renderOrders()
    }

    override fun onResume() {
        super.onResume()
        renderOrders()
    }

    private fun renderOrders() {
        historyContainer.removeAllViews()

        val files = filesDir.listFiles()

        val orderFiles = files?.filter {
            it.name.startsWith("Zamowienie_")
        } ?: emptyList()

        ordersCountText.text = "Liczba zamówień: ${orderFiles.size}"

        if (orderFiles.isEmpty()) {
            val emptyTextView = TextView(this)
            emptyTextView.text = "Brak złożonych zamówień"
            emptyTextView.setTextColor(Color.parseColor("#996B7A"))
            emptyTextView.textSize = 16f
            historyContainer.addView(emptyTextView)
            return
        }

        for (file in orderFiles) {
            val textView = TextView(this)

            textView.text = file.name.replace(".txt", "")
            textView.textSize = 16f
            textView.setTextColor(Color.parseColor("#4E3B42"))

            textView.setPadding(16, 24, 16, 24)
            textView.isClickable = true
            textView.isFocusable = true
            textView.setOnClickListener {
                val intent = Intent(this, OrderPreviewActivity::class.java)
                intent.putExtra("FILE_NAME", file.name)
                startActivity(intent)
            }

            historyContainer.addView(textView)
        }
    }

    private fun setupBottomNav() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationViewProfile)
        bottomNav.selectedItemId = R.id.nav_profile

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_catalog -> {
                    if (this !is MainActivity) {
                        startActivity(Intent(this, MainActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                    }
                    true
                }
                R.id.nav_favorites -> {
                    if (this !is FavoritesActivity) {
                        startActivity(Intent(this, FavoritesActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                    }
                    true
                }
                R.id.nav_cart -> {
                    if (this !is CartActivity) {
                        startActivity(Intent(this, CartActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                    }
                    true
                }
                R.id.nav_profile -> true
                else -> false
            }
        }
    }
}