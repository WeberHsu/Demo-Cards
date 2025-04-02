package com.weberhsu.presentation.ui.cards.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class StackedCardItemDecoration(
    private val overlapHeight: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val totalItemCount = state.itemCount

        if (position == RecyclerView.NO_POSITION) return

        if (position < totalItemCount - 1) {
            outRect.set(0, 0, 0, -overlapHeight)
        }
    }
}