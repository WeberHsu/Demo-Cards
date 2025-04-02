package com.weberhsu.presentation.ui.cards.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.weberhsu.domain.entity.CardEntity
import com.weberhsu.presentation.R
import com.weberhsu.presentation.databinding.ItemCardBinding
import com.weberhsu.presentation.extensions.clickWithTrigger
import java.util.Collections

class CardAdapter : RecyclerView.Adapter<CardAdapter.Companion.CardViewHolder>() {

    companion object {
        class CardViewHolder(view: View) : RecyclerView.ViewHolder(view)
    }

    private var cards: MutableList<CardEntity> = mutableListOf()
    var cardItemClickListener: CardsItemCallback? = null

    fun refreshData(data: MutableList<CardEntity>) {
        cards.clear()
        cards.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val viewBinding = ItemCardBinding.bind(holder.itemView)
        val card = cards[position]
        viewBinding.txtCardName.text = card.cardName
        viewBinding.txtPhysicalCardNumber.text = card.getPhysicalCardNumber()
        viewBinding.txtCardNumber.text = card.getSecretCardNumber()
        viewBinding.layoutRoot.clickWithTrigger {
            cardItemClickListener?.onClickItem(card)
        }
    }


    override fun getItemCount(): Int = cards.size

    fun swapItems(fromPosition: Int, toPosition: Int) {
        Collections.swap(cards, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    interface CardsItemCallback {
        fun onClickItem(card: CardEntity)
    }
}