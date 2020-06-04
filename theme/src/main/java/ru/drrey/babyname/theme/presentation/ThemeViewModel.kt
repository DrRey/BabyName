package ru.drrey.babyname.theme.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import ru.drrey.babyname.common.presentation.base.Action
import ru.drrey.babyname.common.presentation.base.Actor
import ru.drrey.babyname.common.presentation.base.Reducer
import ru.drrey.babyname.theme.R
import ru.drrey.babyname.theme.api.ThemeViewEvent
import ru.drrey.babyname.theme.api.ThemeViewModelApi
import ru.drrey.babyname.theme.api.ThemeViewState
import ru.drrey.babyname.theme.domain.interactor.GetAccentColorInteractor
import ru.drrey.babyname.theme.domain.interactor.GetPrimaryColorInteractor
import ru.drrey.babyname.theme.domain.interactor.SaveAccentColorInteractor
import ru.drrey.babyname.theme.domain.interactor.SavePrimaryColorInteractor

class ThemeViewModel(
    private val getPrimaryColorInteractor: GetPrimaryColorInteractor,
    private val getAccentColorInteractor: GetAccentColorInteractor,
    private val savePrimaryColorInteractor: SavePrimaryColorInteractor,
    private val saveAccentColorInteractor: SaveAccentColorInteractor
) : ThemeViewModelApi() {

    override val viewState by lazy {
        MutableLiveData<ThemeViewState>().apply {
            value = initialViewState
        }
    }
    override val viewEvent by lazy { LiveEvent<ThemeViewEvent>() }

    override val initialViewState = ThemeViewState()
    override val stateReducers = listOf<Reducer<ThemeViewState>>(::reduceThemeViewState)
    override val eventActors = listOf<Actor<ThemeViewEvent>>(::actOnThemeEvent)

    override val primaryColorResId: Int?
        get() = viewState.value?.primaryColorResId

    override val accentColorResId: Int?
        get() = viewState.value?.accentColorResId

    override fun onPrimaryColorChange(colorResId: Int) {
        act(ThemeStateAction.PrimaryColorChanged(colorResId))
        savePrimaryColorInteractor.execute(viewModelScope, colorResId)
    }

    override fun onAccentColorChange(colorResId: Int) {
        act(ThemeStateAction.AccentColorChanged(colorResId))
        saveAccentColorInteractor.execute(viewModelScope, colorResId)
    }

    fun init() {
        getPrimaryColorInteractor.execute(viewModelScope, null, onError = {
            act(ThemeStateAction.PrimaryColorChanged(R.color.colorPrimary))
        }) {
            act(ThemeStateAction.PrimaryColorChanged(it ?: R.color.colorPrimary))
        }
        getAccentColorInteractor.execute(viewModelScope, null, onError = {
            act(ThemeStateAction.AccentColorChanged(R.color.colorAccent))
        }) {
            act(ThemeStateAction.AccentColorChanged(it ?: R.color.colorAccent))
        }
    }

    private fun reduceThemeViewState(
        viewState: ThemeViewState,
        action: Action
    ): ThemeViewState {
        return when (action) {
            is ThemeStateAction.PrimaryColorChanged -> {
                viewState.copy(primaryColorResId = action.colorResId)
            }
            is ThemeStateAction.AccentColorChanged -> {
                viewState.copy(accentColorResId = action.colorResId)
            }
            else -> {
                viewState
            }
        }
    }

    private fun actOnThemeEvent(action: Action): ThemeViewEvent? {
        return null
    }

    sealed class ThemeStateAction : Action {
        class PrimaryColorChanged(val colorResId: Int) : ThemeStateAction()
        class AccentColorChanged(val colorResId: Int) : ThemeStateAction()
    }
}