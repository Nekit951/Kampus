package com.example.kampus2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kampus2.databinding.ViewholderCardBinding
import com.example.kampus2.model.Card
import com.example.kampus2.model.CardDB

class CardAdapter(private val cardList: MutableList<Card>): RecyclerView.Adapter<CardAdapter.Viewholder>() {

    inner class Viewholder(val binding: ViewholderCardBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Viewholder {
        val binding = ViewholderCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(
        holder: Viewholder,
        position: Int
    ) {
        val card = cardList[position]

        holder.binding.apply {
            cardNumber.text = card.number
            cardData.text = card.data
            buttonOptions.setOnClickListener {
                val db = CardDB(holder.itemView.context, null)
                db.delCard(card)
                cardList.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

    override fun getItemCount(): Int = cardList.size
}