package com.example.shilpaskitchen

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class myAdapter(
    private var productList: MutableList<Product>,
    private val onAction: ((Product) -> Unit)? = null, // For add-to-cart in FinalActivity
    private val onCartUpdated: (() -> Unit)? = null,   // For quantity changes in CartActivity
    private val onItemClick: ((Product) -> Unit)? = null // For navigating to detail screen on image click
) : RecyclerView.Adapter<myAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ShapeableImageView = itemView.findViewById(R.id.productImage2)
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productDetail: TextView = itemView.findViewById(R.id.productDetail)
        val productPrice: TextView = itemView.findViewById(R.id.productprice)
        val productQuantity: TextView = itemView.findViewById(R.id.cartquantity)
        val addToCartBtn: Button = itemView.findViewById(R.id.addToCartBtn)
        val btnPlus: Button = itemView.findViewById(R.id.btnPlus)
        val btnMinus: Button = itemView.findViewById(R.id.btnMinus)
        val btnRemove: Button = itemView.findViewById(R.id.btnRemove)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_iteam, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = productList[position]
        holder.productImage.setImageResource(product.imageRes)
        holder.productName.text = product.name
        holder.productPrice.text = "Price : ${product.price} ₹"
        holder.productQuantity.text = "${product.quantity}"

        // Navigate to detail screen when image is clicked
        holder.productImage.setOnClickListener {
            if (onItemClick != null) {
                onItemClick.invoke(product)
            } else {
                val context = holder.itemView.context
                val intent = Intent(context, detail_product::class.java).apply {
                    putExtra("name", product.name)
                    putExtra("price", product.price)
                    putExtra("quantity", product.quantity.toString())
                    putExtra("detail", product.productDetail)
                    putExtra("image", product.imageRes)
                    putExtra("id", product.id)
                }
                context.startActivity(intent)
            }
        }

        if (onAction != null) {
            // FinalActivity mode - show add to cart button
            holder.addToCartBtn.visibility = View.VISIBLE
            holder.btnPlus.visibility = View.GONE
            holder.btnMinus.visibility = View.GONE
            holder.btnRemove.visibility = View.GONE

            holder.addToCartBtn.setOnClickListener {
                onAction.invoke(product)
            }
        } else {
            // CartActivity mode - show quantity control buttons
            holder.addToCartBtn.visibility = View.GONE
            holder.btnPlus.visibility = View.VISIBLE
            holder.btnMinus.visibility = View.VISIBLE
            holder.btnRemove.visibility = View.VISIBLE
            holder.productQuantity.visibility=View.VISIBLE
            holder.btnPlus.setOnClickListener {
                CartManager.increaseQuantity(product)
                notifyItemChanged(position)
                onCartUpdated?.invoke()
            }

            holder.btnMinus.setOnClickListener {
                CartManager.decreaseQuantity(product)
                // Check if item was removed from cart
                if (!CartManager.isInCart(product)) {
                    productList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, productList.size)
                } else {
                    notifyItemChanged(position)
                }
                onCartUpdated?.invoke()
            }

            holder.btnRemove.setOnClickListener {
                CartManager.removeFromCart(product)
                productList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, productList.size)
                onCartUpdated?.invoke()
            }
        }
    }

    override fun getItemCount(): Int = productList.size

    fun updateProductList(newList: MutableList<Product>) {
        productList = newList
        notifyDataSetChanged()
    }
}