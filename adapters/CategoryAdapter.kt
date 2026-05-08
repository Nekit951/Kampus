package com.example.kampus2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.kampus2.R
import com.example.kampus2.databinding.ViewholderCategoryBinding
import com.example.kampus2.model.Category

class CategoryAdapter(private val items: MutableList<Category>, private val categoryClick: (String) -> Unit): RecyclerView.Adapter<CategoryAdapter.Viewholder>() {

    private var selectedPosition = -1
    private var lastSelectedPosition = -1

    fun updateData(newData:List<Category>){
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }

    class Viewholder(val binding: ViewholderCategoryBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Viewholder {
        val binding = ViewholderCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(
        holder: Viewholder,
        position: Int
    ) {
        val item = items[position]
        holder.binding.title.text = item.title

        if(selectedPosition == position){
            holder.binding.title.setBackgroundResource(R.drawable.blue_bg)
            holder.binding.title.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.darkGrey
                )
            )
        }else{
            holder.binding.title.setBackgroundResource(R.drawable.white_bg)
            holder.binding.title.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.black
                )
            )
        }

        holder.binding.root.setOnClickListener {
            val position = position

            if(position != RecyclerView.NO_POSITION){
                lastSelectedPosition = selectedPosition
                selectedPosition = position
                notifyItemChanged(lastSelectedPosition)
                notifyItemChanged(selectedPosition)
                categoryClick(item.title)
            }
        }
    }

    override fun getItemCount(): Int = items.size

}



