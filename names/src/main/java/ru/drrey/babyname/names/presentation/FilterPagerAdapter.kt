package ru.drrey.babyname.names.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import ru.drrey.babyname.names.R
import ru.drrey.babyname.names.api.Sex
import ru.drrey.babyname.names.databinding.FilterPagerItemBinding
import ru.drrey.babyname.names.domain.entity.Name

class FilterPagerAdapter(
    private val names: List<Name>,
    private val namesMap: Map<Name, Boolean?>,
    private val onNameFiltered: (Name, Boolean) -> Unit
) : PagerAdapter() {

    @SuppressLint("InflateParams")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val name = names.getOrNull(position)
        val viewBinding = FilterPagerItemBinding.inflate(LayoutInflater.from(container.context))
        name?.let {
            viewBinding.apply {
                val nameAccentColor = ContextCompat.getColor(
                    root.context,
                    if (name.sex == Sex.BOY) R.color.blue else R.color.pink
                )
                textView.apply {
                    text = name.displayName
                    setTextColor(nameAccentColor)
                }
                yesView.apply {
                    text = context.getString(R.string.yes)
                    setOnClickListener {
                        setBackgroundColor(nameAccentColor)
                        noView.background = null
                        onNameFiltered(name, true)
                    }
                    if (namesMap[name] == true) {
                        setBackgroundColor(nameAccentColor)
                    }
                }
                noView.apply {
                    text = context.getString(R.string.no)
                    setOnClickListener {
                        setBackgroundColor(nameAccentColor)
                        yesView.background = null
                        onNameFiltered(name, false)
                    }
                    if (namesMap[name] == false) {
                        setBackgroundColor(nameAccentColor)
                    }
                }
            }
        }
        container.addView(viewBinding.root)
        return viewBinding.root
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