package ru.drrey.babyname.theme.api

import androidx.lifecycle.ViewModel
import ru.drrey.babyname.common.presentation.base.StateViewModel
import ru.drrey.babyname.common.presentation.base.ViewEvent
import ru.drrey.babyname.common.presentation.base.ViewState

abstract class ThemeViewModelApi : ViewModel(), StateViewModel<ThemeViewState, ThemeViewEvent> {
    abstract val primaryColorResId: Int
    abstract val accentColorResId: Int

    abstract fun onPrimaryColorChange(colorResId: Int)
    abstract fun onAccentColorChange(colorResId: Int)
}

sealed class ThemeViewEvent : ViewEvent

data class ThemeViewState(
    val primaryColorResId: Int = R.color.colorPrimary,
    val accentColorResId: Int = R.color.colorAccent
) : ViewState