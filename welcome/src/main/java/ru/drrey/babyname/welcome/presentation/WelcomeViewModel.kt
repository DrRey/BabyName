package ru.drrey.babyname.welcome.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.drrey.babyname.auth.api.NotLoggedInException
import ru.drrey.babyname.common.domain.interactor.base.Interactor
import ru.drrey.babyname.common.presentation.base.*
import ru.drrey.babyname.names.api.Sex
import ru.drrey.babyname.welcome.R

class WelcomeViewModel(
    private val getUserIdInteractor: Interactor<String, Nothing?>,
    private val getPartnerIdsListInteractor: Interactor<List<String>, Nothing?>,
    private val getSexFilterInteractor: Interactor<Sex?, Nothing?>,
    private val setSexFilterInteractor: Interactor<Unit, Sex?>
) : ViewModel(), StateViewModel<WelcomeViewState, WelcomeViewEvent> {

    override val viewState by lazy {
        MutableLiveData<WelcomeViewState>().apply {
            value = initialViewState
        }
    }
    override val viewEvent by lazy { LiveEvent<WelcomeViewEvent>() }

    override val initialViewState = WelcomeViewState()
    override val stateReducers = listOf<Reducer<WelcomeViewState>>(::reduceWelcomeViewState)
    override val eventActors = listOf<Actor<WelcomeViewEvent>>(::actOnWelcomeEvent)

    fun startWelcome() {
        getUserIdInteractor.execute(viewModelScope, null, onError = {
            if (it is NotLoggedInException) {
                startAuthWelcome()
            }
        }, onSuccess = {
            getPartnerIdsListInteractor.execute(viewModelScope, null) {
                if (it.isNullOrEmpty()) {
                    startPartnerWelcome()
                } else {
                    getSexFilterInteractor.execute(viewModelScope, null) { sexFilter ->
                        if (sexFilter == null) {
                            startSexWelcome()
                        } else {
                            finishWelcome()
                        }
                    }
                }
            }
        })
    }

    private fun startAuthWelcome() {
        viewModelScope.launch {
            delay(1000L)
            act(WelcomeStateAction.ShowText(R.string.hello))
            delay(2000L)
            act(WelcomeStateAction.HideText)
            delay(2000L)
            act(WelcomeStateAction.ShowText(R.string.welcome_auth))
            delay(2000L)
            act(WelcomeStateAction.HideText)
            delay(1000L)
            act(WelcomeStateAction.StartAuth)
        }
    }

    private fun startPartnerWelcome() {
        viewModelScope.launch {
            act(WelcomeStateAction.HideText)
            delay(1000L)
            act(WelcomeStateAction.ShowPartnerScreen)
        }
    }

    fun onPartnersFinished() {
        startSexWelcome()
    }

    private fun startSexWelcome() {
        viewModelScope.launch {
            act(WelcomeStateAction.HideText)
            delay(1000L)
            act(WelcomeStateAction.ShowSexScreen)
        }
    }

    fun onSexSet(sex: Sex?) {
        setSexFilterInteractor.execute(viewModelScope, sex, onSuccess = {
            finishWelcome()
        })
    }

    private fun finishWelcome() {
        viewModelScope.launch {
            act(WelcomeStateAction.HideText)
            delay(1000L)
            act(WelcomeStateAction.ShowText(R.string.congrats))
            delay(2000L)
            act(WelcomeStateAction.HideText)
            delay(2000L)
            act(WelcomeStateAction.ShowText(R.string.welcome_ready))
            delay(2000L)
            act(WelcomeStateAction.HideText)
            delay(1000L)
            act(WelcomeStateAction.WelcomeFinished)
        }
    }

    private fun reduceWelcomeViewState(
        viewState: WelcomeViewState,
        action: Action
    ): WelcomeViewState {
        return when (action) {
            is WelcomeStateAction.ShowText -> {
                viewState.copy(
                    textShown = true,
                    textResId = action.textResId,
                    partnerButtonsShown = false,
                    sexButtonsShown = false
                )
            }
            WelcomeStateAction.HideText -> {
                viewState.copy(
                    textShown = false,
                    partnerButtonsShown = false,
                    sexButtonsShown = false
                )
            }
            WelcomeStateAction.ShowPartnerScreen -> {
                viewState.copy(
                    textShown = true,
                    textResId = R.string.welcome_partner,
                    partnerButtonsShown = true,
                    sexButtonsShown = false
                )
            }
            WelcomeStateAction.ShowSexScreen -> {
                viewState.copy(
                    textShown = true,
                    textResId = R.string.welcome_sex,
                    partnerButtonsShown = false,
                    sexButtonsShown = true
                )
            }
            else -> {
                viewState
            }
        }
    }
}

private fun actOnWelcomeEvent(action: Action): WelcomeViewEvent? {
    return when (action) {
        WelcomeStateAction.StartAuth -> {
            WelcomeViewEvent.StartAuth
        }
        WelcomeStateAction.WelcomeFinished -> {
            WelcomeViewEvent.WelcomeFinished
        }
        else -> {
            null
        }
    }
}

sealed class WelcomeStateAction : Action {
    class ShowText(val textResId: Int) : WelcomeStateAction()
    object HideText : WelcomeStateAction()
    object ShowPartnerScreen : WelcomeStateAction()
    object ShowSexScreen : WelcomeStateAction()
    object StartAuth : WelcomeStateAction()
    object WelcomeFinished : WelcomeStateAction()
}

sealed class WelcomeViewEvent : ViewEvent {
    object StartAuth : WelcomeViewEvent()
    object WelcomeFinished : WelcomeViewEvent()
}

data class WelcomeViewState(
    val textShown: Boolean = false,
    val textResId: Int? = null,
    val partnerButtonsShown: Boolean = false,
    val sexButtonsShown: Boolean = false
) : ViewState