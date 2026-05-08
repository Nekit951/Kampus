package com.example.kampus2.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kampus2.helpers.ChangeNumberItemsListener
import com.example.kampus2.helpers.ManagmentCart
import com.example.kampus2.model.ItemsModel
import com.example.kampus2.databinding.ViewholderCartBinding

class CartAdapter(private val listItemSelected: ArrayList<ItemsModel>, private val context: Context, var changeNumberItemsListener: ChangeNumberItemsListener? = null):
    RecyclerView.Adapter<CartAdapter.Viewholder>() {

    private val managmentCart = ManagmentCart(context)

    class Viewholder(val binding: ViewholderCartBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Viewholder {
        val binding = ViewholderCartBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(
        holder: Viewholder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val item = listItemSelected[position]
        holder.binding.apply {
            title.text = item.title
            feeEachItemTxt.text = "${item.price} руб."
            totalEachItem.text = "${Math.round(item.numberInCart*item.price)}"
            countTxt.text = item.numberInCart.toString()

            Glide.with(holder.itemView.context).load(item.picUrl).into(picCart)

            btnAdd.setOnClickListener {
                managmentCart.plusItem(listItemSelected, position, object: ChangeNumberItemsListener{
                    override fun onChanged() {
                        notifyDataSetChanged()
                        changeNumberItemsListener?.onChanged()
                    }
                })
            }

            btnDel.setOnClickListener {
                managmentCart.minusItem(listItemSelected, position, object: ChangeNumberItemsListener{
                    override fun onChanged() {
                        notifyDataSetChanged()
                        changeNumberItemsListener?.onChanged()
                    }
                })
            }
        }
    }

    override fun getItemCount(): Int = listItemSelected.size
}