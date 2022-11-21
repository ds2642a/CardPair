package com.dmitri.cardpair.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dmitri.cardpair.databinding.ItemCardBinding
import com.dmitri.cardpair.model.Card

class CardAdapter(private val cards: List<Card>, private val onClick: (Int) -> Unit) :
    RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    private lateinit var context : Context;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context;
        return ViewHolder(
            ItemCardBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = cards[position]

        if (card.isSolved) {
            holder.binding.image.visibility = View.GONE
            return
        } else {
            holder.binding.image.visibility = View.VISIBLE
        }

        if (!card.isShown) {
            holder.binding.image.setImageResource(card.backImageResId)
        } else {
            holder.binding.image.setImageDrawable(card.imageDrawable)
        }

        holder.bind(position, onClick)
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    inner class ViewHolder (
        val binding: ItemCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int, onClick:(Int) -> Unit) {
            binding.root.setOnClickListener {
                onClick(position)
            }
        }
    }
}