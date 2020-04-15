package ru.drrey.babyname.names.presentation

import androidx.core.content.ContextCompat
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.name_item.view.*
import ru.drrey.babyname.names.R
import ru.drrey.babyname.names.domain.entity.Name

class NameItem(val name: Name, private val onClickListener: (Name, Int, Int) -> Unit) :
    Item<ViewHolder>() {
    override fun getLayout() = R.layout.name_item

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.apply {
            nameView.text = name.displayName
            val activeColor = ContextCompat.getColor(context, R.color.colorAccent)
            val inactiveColor = ContextCompat.getColor(context, R.color.colorPrimary)
            star1View.setBackgroundColor(inactiveColor)
            star2View.setBackgroundColor(inactiveColor)
            star3View.setBackgroundColor(inactiveColor)
            star4View.setBackgroundColor(inactiveColor)
            star5View.setBackgroundColor(inactiveColor)
            when (name.stars) {
                1 -> {
                    star1View.setBackgroundColor(activeColor)
                }
                2 -> {
                    star2View.setBackgroundColor(activeColor)
                }
                3 -> {
                    star3View.setBackgroundColor(activeColor)
                }
                4 -> {
                    star4View.setBackgroundColor(activeColor)
                }
                5 -> {
                    star5View.setBackgroundColor(activeColor)
                }
            }

            star1View.setOnClickListener { onClickListener.invoke(name, position, 1) }
            star2View.setOnClickListener { onClickListener.invoke(name, position, 2) }
            star3View.setOnClickListener { onClickListener.invoke(name, position, 3) }
            star4View.setOnClickListener { onClickListener.invoke(name, position, 4) }
            star5View.setOnClickListener { onClickListener.invoke(name, position, 5) }
        }
    }
}