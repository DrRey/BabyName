package ru.drrey.babyname.common.presentation.base

import androidx.lifecycle.Observer

/**
 * An [Observer] that fires only on non-null items.
 */
class NonNullObserver<T>(private val onChanged: (T) -> Unit) : Observer<T> {
    override fun onChanged(value: T) {
        value?.let {
            onChanged.invoke(it)
        }
    }
}