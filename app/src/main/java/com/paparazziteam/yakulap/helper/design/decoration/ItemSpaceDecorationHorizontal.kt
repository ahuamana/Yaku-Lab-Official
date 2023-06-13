package com.paparazziteam.yakulap.helper.design.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemSpaceDecorationHorizontal(space: Int) : RecyclerView.ItemDecoration() {
    private var space: Int = 0
    init {
        this.space = space
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.top = space
        outRect.right = space
        outRect.bottom = space
        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = space
        }
    }
}