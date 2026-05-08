package com.example.kampus2.adapters

import android.R.attr.order
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kampus2.model.ItemsModel
import com.example.kampus2.model.OrderModel
import com.example.kampus2.databinding.ViewholderOrderBinding

class OrderAdapter(private val ordersList: ArrayList<OrderModel>, private val itemClick: (OrderModel, Int) -> Unit): RecyclerView.Adapter<OrderAdapter.Viewholder>() {

    class Viewholder(val binding: ViewholderOrderBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Viewholder {
        val binding = ViewholderOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(
        holder: Viewholder,
        position: Int
    ) {
        val order = ordersList[position]

        val allProductsName = order.items.joinToString(", ") {it.title}
        val allQuanties = order.items.joinToString(", ") {"${it.numberInCart}"}

        holder.binding.apply {
            title.text = allProductsName
            finalPrice.text = "${order.summa} руб."
            countTxt.text = "${allQuanties} шт."
            orderAddress.text = order.address

            Glide.with(holder.itemView.context).load(order.items[0].picUrl).into(picOrder)

            root.setOnClickListener {
                itemClick(order, position)
            }
        }
    }

    override fun getItemCount(): Int =  ordersList.size
}