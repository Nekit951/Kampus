package com.example.kampus2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kampus2.databinding.ViewholderOrderDetailBinding
import com.example.kampus2.model.ItemsModel

class OrderDetailAdapter(private val listItemSelected: ArrayList<ItemsModel>): RecyclerView.Adapter<OrderDetailAdapter.Viewholder>() {

    class Viewholder(val binding: ViewholderOrderDetailBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Viewholder {
        val binding = ViewholderOrderDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(
        holder: Viewholder,
        position: Int
    ) {
        val item = listItemSelected[position]

        holder.binding.apply {
            title.text = item.title

            Glide.with(holder.itemView.context).load(item.picUrl).into(picOrderDetail)
        }
    }

    override fun getItemCount(): Int = listItemSelected.size
}