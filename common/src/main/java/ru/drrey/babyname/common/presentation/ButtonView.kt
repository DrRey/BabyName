package ru.drrey.babyname.common.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import kotlinx.android.synthetic.main.view_button.view.*
import ru.drrey.babyname.common.R

class ButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        View.inflate(context, R.layout.view_button, this)
    }

    var text: String
        get() = textView?.text?.toString() ?: ""
        set(value) {
            textView?.text = value
        }
}