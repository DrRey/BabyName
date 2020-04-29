package ru.drrey.babyname.welcome.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import ru.drrey.babyname.common.presentation.base.*

class WelcomeViewModel : ViewModel(), StateViewModel<WelcomeViewState, WelcomeViewEvent> {

    override val viewState by lazy {
        MutableLiveData<WelcomeViewState>().apply {
            value = initialViewState
        }
    }
    override val viewEvent by lazy { LiveEvent<WelcomeViewEvent>() }

    override val initialViewState = WelcomeViewState()
    override val stateReducers = listOf<Reducer<WelcomeViewState>>(::reduceWelcomeViewState)
    override val eventActors = listOf<Actor<WelcomeViewEvent>>(::actOnWelcomeEvent)

    private fun reduceWelcomeViewState(
        viewState: WelcomeViewState,
        action: Action
    ): WelcomeViewState {
        return viewState
    }
}

private fun actOnWelcomeEvent(action: Action): WelcomeViewEvent? {
    return null
}

sealed class WelcomeStateAction : Action

sealed class WelcomeViewEvent : ViewEvent

data class WelcomeViewState(
    val showOverlay: Boolean = true,
    val error: String? = null,
    val isLoggedIn: Boolean = false,
    val partnersCount: Int = 0,
    val starredNamesCount: Int = 0
) : ViewState