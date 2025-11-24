package com.example.shilpaskitchen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shilpaskitchen.data.Order
import com.example.shilpaskitchen.data.OrderDao

class AdminOrdersAdapter(
    private val orders: List<Order>,
    private val orderDao: OrderDao
) : RecyclerView.Adapter<AdminOrdersAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val subtitle: TextView = itemView.findViewById(R.id.subtitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders[position]
        holder.title.text = "${order.userEmail} — ₹${order.totalAmount}"
        holder.subtitle.text = "${order.status} • ${java.text.SimpleDateFormat("dd MMM, HH:mm").format(java.util.Date(order.createdAt))}"
    }

    override fun getItemCount(): Int = orders.size
}




