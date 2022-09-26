package ru.drrey.babyname.common.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import ru.drrey.babyname.common.databinding.ViewButtonBinding

class ButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var viewBinding: ViewButtonBinding

    init {
        viewBinding = ViewButtonBinding.inflate(LayoutInflater.from(context), this)
    }

    var text: String
        get() = viewBinding.textView.text?.toString() ?: ""
        set(value) {
            viewBinding.textView.text = value
        }
}