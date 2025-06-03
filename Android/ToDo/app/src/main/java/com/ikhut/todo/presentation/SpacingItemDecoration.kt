package com.ikhut.todo.presentation

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class SpacingItemDecoration(
    private val horizontalSpacing: Int, private val verticalSpacing: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)

        when (val layoutManager = parent.layoutManager) {
            is StaggeredGridLayoutManager -> {
                val layoutParams = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
                val spanIndex = layoutParams.spanIndex
                val spanCount = layoutManager.spanCount

                outRect.left = horizontalSpacing * spanIndex / spanCount
                outRect.right = horizontalSpacing * (spanCount - 1 - spanIndex) / spanCount

                if (position >= spanCount) {
                    outRect.top = verticalSpacing
                }
            }

            else -> {
                if (position > 0) {
                    outRect.top = verticalSpacing
                }
            }
        }
    }
}