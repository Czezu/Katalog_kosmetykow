package com.example.projekt_czezowska_dominika_kosmetyki

object ProductRepository {
    val list = listOf(
        Product(1, "Mleczko różane", 49.99, true, Category.PIELĘGNACJA, R.drawable.mleczko_rozane, "Aksamitne mleczko różane do pielęgnacji wrażliwej skóry twarzy.", "Pojemność: 50 ml"),
        Product(3, "Bananowy żel do mycia", 29.90, true, Category.PIELĘGNACJA, R.drawable.zel_bananowy, "Głęboko oczyszczający żel o pięknym, intensywnym zapachu banana.", "Pojemność: 200 ml"),
        Product(6, "Peeling matcha", 44.00, false, Category.PIELĘGNACJA, R.drawable.peeling_matcha, "Naturalny peeling z dodatkiem zielonej herbaty matcha.", "Waga: 150 g"),
        Product(7, "Serum witaminowe", 65.00, true, Category.PIELĘGNACJA, R.drawable.serum_witaminowe, "Rozświetlające serum z witaminą C.", "Pojemność: 30 ml"),

        Product(2, "Tusz do rzęs Panda", 39.90, false, Category.MAKIJAŻ, R.drawable.tusz_panda, "Uroczy tusz do rzęs maksymalnie wydłużający włoski, wodoodporny.", "Wodoodporny"),
        Product(5, "Błyszczyk do ust Lolita", 34.50, true, Category.MAKIJAŻ, R.drawable.blyszczyk_truskawkowy, "Lśniący błyszczyk.", "Zapach: Truskawkowy"),
        Product(8, "Paletka cieni Pastel", 79.99, false, Category.MAKIJAŻ, R.drawable.paletka_pastel, "Zestaw pastelowych cieni do powiek.", "Pastelowa paletka"),

        Product(4, "Mikstura spa kąpiel", 59.00, false, Category.KĄPIEL, R.drawable.mikstura_spa, "Relaksująca mikstura do kąpieli, bogata w olejki eteryczne.", "Pojemność: 250 ml"),
        Product(9, "Kula do kąpieli Lawenda", 15.50, true, Category.KĄPIEL, R.drawable.kula_lawenda, "Musująca kula do kąpieli o uspokajającym zapachu lawendy.", "Waga: 120 g")
    )

    fun getProductById(id: Int): Product? {
        return list.find { it.id == id }
    }
}