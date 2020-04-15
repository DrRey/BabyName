package ru.drrey.babyname.common.presentation

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import ru.drrey.babyname.common.R

class VerticalSpaceDivider(context: Context) :
    DividerItemDecoration(context, RecyclerView.VERTICAL) {
    init {
        ContextCompat.getDrawable(context, R.drawable.divider_vertical)?.let {
            setDrawable(it)
        }
    }
}