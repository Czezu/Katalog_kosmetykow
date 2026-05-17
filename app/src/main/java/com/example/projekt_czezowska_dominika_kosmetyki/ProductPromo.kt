package com.example.projekt_czezowska_dominika_kosmetyki

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductPromo(
    private var productsList: List<Product>,
    private val onProductClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductPromo.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProduct: ImageView = view.findViewById(R.id.itemProductImage)
        val tvName: TextView = view.findViewById(R.id.itemProductName)
        val tvPrice: TextView = view.findViewById(R.id.itemProductPrice)
        val tvPromoBadge: TextView = view.findViewById(R.id.itemProductPromoBadge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val produkt = productsList[position]
        holder.tvName.text = produkt.name
        holder.tvPrice.text = "${produkt.price} zł"
        holder.imgProduct.setImageResource(produkt.imageResId)

        if (produkt.isPromo) {
            holder.tvPromoBadge.visibility = View.VISIBLE
        } else {
            holder.tvPromoBadge.visibility = View.GONE
        }

        holder.itemView.setOnClickListener { onProductClick(produkt) }
    }

    override fun getItemCount(): Int = productsList.size

    fun updateData(newList: List<Product>) {
        this.productsList = newList
        notifyDataSetChanged()
    }
}