package ru.drrey.babyname.results.presentation

import android.view.View
import androidx.core.content.ContextCompat
import com.xwray.groupie.viewbinding.BindableItem
import ru.drrey.babyname.results.R
import ru.drrey.babyname.results.databinding.ItemResultBinding
import ru.drrey.babyname.results.domain.entity.Result

class ResultItem(private val result: Result, private val accentColorResId: Int?) :
    BindableItem<ItemResultBinding>() {
    override fun getLayout() = R.layout.item_result

    override fun bind(binding: ItemResultBinding, position: Int) {
        binding.apply {
            nameView.text = result.name
            starsView.text = result.averageStars.toString()
            accentColorResId?.let {
                val color = ContextCompat.getColor(root.context, it)
                nameView.setTextColor(color)
                starsView.setTextColor(color)
            }
        }
    }

    override fun initializeViewBinding(view: View): ItemResultBinding {
        return ItemResultBinding.bind(view)
    }
}