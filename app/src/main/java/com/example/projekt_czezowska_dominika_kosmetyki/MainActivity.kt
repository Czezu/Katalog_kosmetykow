package com.example.projekt_czezowska_dominika_kosmetyki

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val pelnaListaProduktow = listOf(
        Product(
            id = 1,
            name = "Mleczko różane",
            price = 49.99,
            category = "pielęgnacja",
            isPromo = true,
            description = "Delikatne mleczko z ekstraktem z płatków róż. Głęboko nawilża, łagodzi podrażnienia i nadaje skórze zdrowy wygląd.",
            imageResId = R.drawable.mleczko_rozane
        ),
        Product(
            id = 2,
            name = "Błyszczyk truskawkowy",
            price = 39.90,
            category = "makijaż",
            isPromo = false,
            description = "Truskawkowy błyszczyk do ust, który nadaje błyszczące wykończenie bez uczucia lepkości, jednocześnie pielęgnując i chroniąc skórę ust.",
            imageResId = R.drawable.blyszczyk_truskawkowy
        )
    )

    private lateinit var adapter: ProductPromo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchInput = findViewById<EditText>(R.id.searchInput)
        val categorySpinner = findViewById<Spinner>(R.id.categorySpinner)
        val sortRadioGroup = findViewById<RadioGroup>(R.id.sortRadioGroup)
        val promoCheckbox = findViewById<CheckBox>(R.id.promoCheckbox)
        val applyFiltersBtn = findViewById<Button>(R.id.applyFiltersBtn)
        val productCountText = findViewById<TextView>(R.id.productCountText)
        val recyclerView = findViewById<RecyclerView>(R.id.productsRecyclerView)

        val kategorie = arrayOf("Wszystkie", "pielęgnacja", "makijaż")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, kategorie)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = spinnerAdapter

        adapter = ProductPromo(pelnaListaProduktow) { wybranyProdukt ->
            val intent = Intent(this, ProductDetailsActivity::class.java)
            intent.putExtra("PRODUCT_DATA", wybranyProdukt)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        productCountText.text = "${pelnaListaProduktow.size} produktów"

        applyFiltersBtn.setOnClickListener {
            val tekstWyszukiwania = searchInput.text.toString().trim()
            val wybranaKategoria = categorySpinner.selectedItem.toString()
            val tylkoPromocje = promoCheckbox.isChecked
            val zaznaczoneSortowanieId = sortRadioGroup.checkedRadioButtonId

            var przefiltrowanaLista = pelnaListaProduktow.filter { produkt ->
                val pasujeDoSzukaj = produkt.name.contains(tekstWyszukiwania, ignoreCase = true) ||
                        produkt.description.contains(tekstWyszukiwania, ignoreCase = true)

                val pasujeDoKategorii = wybranaKategoria == "Wszystkie" ||
                        produkt.category.equals(wybranaKategoria, ignoreCase = true)

                val pasujeDoPromocji = !tylkoPromocje || produkt.isPromo

                pasujeDoSzukaj && pasujeDoKategorii && pasujeDoPromocji
            }

            przefiltrowanaLista = when (zaznaczoneSortowanieId) {
                R.id.radioPriceAsc -> przefiltrowanaLista.sortedBy { it.price }
                R.id.radioPriceDesc -> przefiltrowanaLista.sortedByDescending { it.price }
                R.id.radioNameAsc -> przefiltrowanaLista.sortedBy { it.name }
                else -> przefiltrowanaLista
            }

            adapter.updateData(przefiltrowanaLista)

            productCountText.text = "${przefiltrowanaLista.size} produktów"
        }
    }
}