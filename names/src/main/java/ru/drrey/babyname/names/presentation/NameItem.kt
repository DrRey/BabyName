package ru.drrey.babyname.names.presentation

import androidx.core.content.ContextCompat
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_name.view.*
import ru.drrey.babyname.names.R
import ru.drrey.babyname.names.domain.entity.Name
import ru.drrey.babyname.theme.api.ThemeViewModelApi

class NameItem(
    private val name: Name,
    private val themeViewModelApi: ThemeViewModelApi,
    private val onClickListener: (Name, Int, Int) -> Unit
) :
    Item<ViewHolder>() {
    override fun getLayout() = R.layout.item_name

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.apply {
            nameView.text = name.displayName
            val activeColor = ContextCompat.getColor(
                context,
                themeViewModelApi.accentColorResId ?: R.color.colorAccent
            )
            val inactiveColor = ContextCompat.getColor(context, R.color.grey)
            nameView.setTextColor(activeColor)
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

    override fun getId(): Long {
        return name.hashCode().toLong()
    }

    override fun equals(other: Any?): Boolean {
        return (other as? NameItem)?.let {
            this.name == it.name
        } ?: false
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + themeViewModelApi.hashCode()
        result = 31 * result + onClickListener.hashCode()
        return result
    }
}