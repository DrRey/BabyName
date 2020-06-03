package ru.drrey.babyname.names.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.filter_pager_item.view.*
import ru.drrey.babyname.names.R
import ru.drrey.babyname.names.api.Sex
import ru.drrey.babyname.names.domain.entity.Name

class FilterPagerAdapter(
    private val names: List<Name>,
    private val namesMap: Map<Name, Boolean?>,
    private val onNameFiltered: (Name, Boolean) -> Unit
) : PagerAdapter() {

    @SuppressLint("InflateParams")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.filter_pager_item, null)
        val name = names.getOrNull(position)
        name?.let {
            view.apply {
                val nameAccentColor = ContextCompat.getColor(
                    context,
                    if (name.sex == Sex.BOY) R.color.blue else R.color.pink
                )
                textView?.apply {
                    text = name.displayName
                    setTextColor(nameAccentColor)
                }
                yesView?.apply {
                    text = context.getString(R.string.yes)
                    setOnClickListener {
                        setBackgroundColor(nameAccentColor)
                        view.noView?.background = null
                        onNameFiltered(name, true)
                    }
                    if (namesMap[name] == true) {
                        setBackgroundColor(nameAccentColor)
                    }
                }
                noView?.apply {
                    text = context.getString(R.string.no)
                    setOnClickListener {
                        setBackgroundColor(nameAccentColor)
                        view.yesView?.background = null
                        onNameFiltered(name, false)
                    }
                    if (namesMap[name] == false) {
                        setBackgroundColor(nameAccentColor)
                    }
                }
            }
        }
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return (view === obj)
    }

    override fun getCount(): Int {
        return names.count()
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}