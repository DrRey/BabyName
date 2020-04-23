package ru.drrey.babyname.common.presentation

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import ru.drrey.babyname.common.R
import ru.drrey.babyname.navigation.Router
import ru.drrey.babyname.navigation.RouterProvider

val Activity.router
    get() = (this as? RouterProvider)?.router

val Fragment.router: Router?
    get() = ((this as? RouterProvider)?.router) ?: (parentFragment?.router)
    ?: ((activity as? RouterProvider)?.router)

fun Fragment.getChildFragmentOrItself(fragmentHolderResId: Int = R.id.fragmentHolder): Fragment {
    return takeIf { isAdded }
        ?.childFragmentManager?.findFragmentById(fragmentHolderResId)
        ?.getChildFragmentOrItself(fragmentHolderResId)
        ?: this
}

inline fun <T : View> T.doOnMeasure(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}

inline fun <T : View> T.doOnPreDraw(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            viewTreeObserver.removeOnPreDrawListener(this)
            f()
            return true
        }
    })
}

fun View.updatePadding(
    left: Int = paddingLeft,
    top: Int = paddingTop,
    right: Int = paddingRight,
    bottom: Int = paddingBottom
) {
    setPadding(left, top, right, bottom)
}

fun Activity.hideKeyboard() {
    currentFocus?.run {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}

fun Float.toDp(ctx: Context): Float = this / ctx.resources.displayMetrics.density

fun Float.toPx(ctx: Context): Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, ctx.resources.displayMetrics)