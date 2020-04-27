package ru.drrey.babyname.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import ru.drrey.babyname.common.domain.interactor.base.Interactor
import ru.drrey.babyname.common.presentation.base.*
import ru.drrey.babyname.domain.interactor.CheckWelcomeScreenShownInteractor

class MainViewModel(
    private val checkWelcomeScreenShownInteractor: CheckWelcomeScreenShownInteractor,
    private val getUserIdInteractor: Interactor<String, Void?>,
    private val getPartnerIdsListInteractor: Interactor<List<String>, Void?>,
    private val clearPartnersInteractor: Interactor<Nothing, Void?>,
    private val getStarredNamesInteractor: Interactor<Int, Void?>
) : ViewModel(), StateViewModel<MainViewState, MainViewEvent> {

    override val viewState by lazy {
        MutableLiveData<MainViewState>().apply {
            value = initialViewState
        }
    }
    override val viewEvent by lazy { LiveEvent<MainViewEvent>() }

    override val initialViewState = MainViewState()
    override val stateReducers = listOf<Reducer<MainViewState>>(::reduceMainViewState)
    override val eventActors = listOf<Actor<MainViewEvent>>(::actOnMainEvent)

    fun loadData() {
        act(MainStateAction.LoadingStarted)
        checkWelcomeScreenShownInteractor.execute(viewModelScope, null) {
            act(MainStateAction.WelcomeScreenNeeded(!it))
        }
        getUserIdInteractor.execute(
            viewModelScope,
            null,
            onError = { act(MainStateAction.LoadError(it.message ?: "")) }) { userId ->
            act(MainStateAction.LoadedUserId(userId))
            getPartnerIdsListInteractor.execute(
                viewModelScope,
                null,
                onError = {
                    act(MainStateAction.LoadError(it.message ?: ""))
                }) { partnerIds ->
                act(MainStateAction.LoadedPartners(partnerIds))
                getStarredNamesInteractor.execute(
                    viewModelScope,
                    null,
                    onError = {
                        act(MainStateAction.LoadError(it.message ?: ""))
                    }) {
                    act(MainStateAction.LoadedStarredNames(it))
                    act(MainStateAction.LoadingFinished)
                }
            }

        }
    }

    fun onClearPartners() {
        clearPartnersInteractor.execute(viewModelScope, null) {
            loadData()
        }
    }

    private fun reduceMainViewState(viewState: MainViewState, action: Action): MainViewState {
        return when (action) {
            MainStateAction.LoadingStarted -> {
                viewState.copy(isLoading = true, error = null)
            }
            is MainStateAction.LoadError -> {
                viewState.copy(isLoading = false, error = action.message)
            }
            is MainStateAction.WelcomeScreenNeeded -> {
                viewState.copy(welcomeScreenNeeded = action.needed)
            }
            is MainStateAction.LoadedUserId -> {
                viewState.copy(isLoggedIn = true)
            }
            is MainStateAction.LoadedPartners -> {
                viewState.copy(partnersCount = action.partnerIds.size)
            }
            is MainStateAction.LoadedStarredNames -> {
                viewState.copy(starredNamesCount = action.count)
            }
            MainStateAction.LoadingFinished -> {
                viewState.copy(isLoading = false)
            }
            else -> {
                viewState
            }
        }
    }

    private fun actOnMainEvent(action: Action): MainViewEvent? {
        return null
    }

    sealed class MainStateAction : Action {
        object LoadingStarted : MainStateAction()
        class LoadError(val message: String) : MainStateAction()
        class WelcomeScreenNeeded(val needed: Boolean) : MainStateAction()
        class LoadedUserId(val userId: String) : MainStateAction()
        class LoadedPartners(val partnerIds: List<String>) : MainStateAction()
        class LoadedStarredNames(val count: Int) : MainStateAction()
        object LoadingFinished : MainStateAction()
    }
}

sealed class MainViewEvent : ViewEvent

data class MainViewState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val welcomeScreenNeeded: Boolean = false,
    val isLoggedIn: Boolean = false,
    val partnersCount: Int = 0,
    val starredNamesCount: Int = 0
) : ViewState