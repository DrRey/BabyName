package ru.drrey.babyname.results.presentation

import androidx.core.content.ContextCompat
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_result.view.*
import ru.drrey.babyname.results.R
import ru.drrey.babyname.results.domain.entity.Result

class ResultItem(private val result: Result, private val accentColorResId: Int?) : Item() {
    override fun getLayout() = R.layout.item_result

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            nameView.text = result.name
            starsView.text = result.averageStars.toString()
            accentColorResId?.let {
                val color = ContextCompat.getColor(context, it)
                nameView.setTextColor(color)
                starsView.setTextColor(color)
            }
        }
    }
}