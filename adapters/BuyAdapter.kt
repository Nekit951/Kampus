package com.example.kampus2.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kampus2.databinding.ViewholderBuyBinding
import com.example.kampus2.helpers.ManagmentCart
import com.example.kampus2.model.ItemsModel


class BuyAdapter(private val listItemSelected: ArrayList<ItemsModel>, private val context: Context): RecyclerView.Adapter<BuyAdapter.Viewholder>() {

    class Viewholder(val binding: ViewholderBuyBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Viewholder {
        val binding = ViewholderBuyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(
        holder: Viewholder,
        position: Int
    ) {
        val item = listItemSelected[position]
        holder.binding.apply {
            title.text = item.title
            totalEachItem.text = "${Math.round(item.numberInCart*item.price)}"
            countTxt.text = item.numberInCart.toString()

            Glide.with(holder.itemView.context).load(item.picUrl).into(picBuy)
        }
    }

    override fun getItemCount(): Int = listItemSelected.size
}