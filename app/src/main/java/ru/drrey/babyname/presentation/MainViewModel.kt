package ru.drrey.babyname.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import ru.drrey.babyname.auth.api.NotLoggedInException
import ru.drrey.babyname.common.domain.interactor.base.Interactor
import ru.drrey.babyname.common.presentation.base.*
import ru.drrey.babyname.domain.interactor.CheckWelcomeScreenShownInteractor
import ru.drrey.babyname.names.api.Sex

class MainViewModel(
    private val checkWelcomeScreenShownInteractor: CheckWelcomeScreenShownInteractor,
    private val getUserIdInteractor: Interactor<String, Nothing?>,
    private val getPartnerIdsListInteractor: Interactor<List<String>, Nothing?>,
    private val clearPartnersInteractor: Interactor<Unit, Nothing?>,
    private val getSexFilterInteractor: Interactor<Sex?, Nothing?>,
    private val setSexFilterInteractor: Interactor<Unit, Sex?>,
    private val countStarredNamesInteractor: Interactor<Int, Nothing?>,
    private val countUnfilteredNamesInteractor: Interactor<Int, Nothing?>
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
        checkWelcomeScreenShownInteractor.execute(viewModelScope, null) { welcomeScreenShown ->
            if (!welcomeScreenShown) {
                act(MainStateAction.WelcomeScreenNeeded)
            } else {
                getUserIdInteractor.execute(
                    viewModelScope,
                    null,
                    onError = { error ->
                        if (error is NotLoggedInException) {
                            act(MainStateAction.LoadingFinished)
                        } else {
                            act(MainStateAction.LoadError(error.message ?: ""))
                        }
                    }) { userId ->
                    act(MainStateAction.LoadedUserId(userId))
                    getSexFilterInteractor.execute(viewModelScope, null) { sexFilter ->
                        act(MainStateAction.LoadedSexFilter(sexFilter))
                    }
                    getPartnerIdsListInteractor.execute(
                        viewModelScope,
                        null,
                        onError = { error ->
                            act(MainStateAction.LoadError(error.message ?: ""))
                        }) { partnerIds ->
                        act(MainStateAction.LoadedPartners(partnerIds))
                        countUnfilteredNamesInteractor.execute(viewModelScope, null,
                            onError = { error ->
                                act(MainStateAction.LoadError(error.message ?: ""))
                            }) { unfilteredNamesCount ->
                            act(MainStateAction.LoadedUnfilteredNames(unfilteredNamesCount))
                            countStarredNamesInteractor.execute(
                                viewModelScope,
                                null,
                                onError = { error ->
                                    act(MainStateAction.LoadError(error.message ?: ""))
                                }) { starredNames ->
                                act(MainStateAction.LoadedStarredNames(starredNames))
                                act(MainStateAction.LoadingFinished)
                            }
                        }
                    }
                }
            }
        }
    }

    fun onClearPartners() {
        clearPartnersInteractor.execute(viewModelScope, null) {
            loadData()
        }
    }

    fun onSexSet(sex: Sex?) {
        setSexFilterInteractor.execute(viewModelScope, sex) {
            act(MainStateAction.LoadedSexFilter(sex))
            loadData()
        }
    }

    private fun reduceMainViewState(viewState: MainViewState, action: Action): MainViewState {
        return when (action) {
            MainStateAction.LoadingStarted -> {
                viewState.copy(sexFilterLoaded = false, error = null, isLoadingData = true)
            }
            is MainStateAction.LoadError -> {
                viewState.copy(error = action.message, isLoadingData = false)
            }
            is MainStateAction.LoadedUserId -> {
                viewState.copy(isLoggedIn = true)
            }
            is MainStateAction.LoadedPartners -> {
                viewState.copy(partnersCount = action.partnerIds.size)
            }
            is MainStateAction.LoadedSexFilter -> {
                viewState.copy(sexFilterLoaded = true, sexFilter = action.sex)
            }
            is MainStateAction.LoadedUnfilteredNames -> {
                viewState.copy(unfilteredNamesCount = action.count)
            }
            is MainStateAction.LoadedStarredNames -> {
                viewState.copy(starredNamesCount = action.count)
            }
            MainStateAction.LoadingFinished -> {
                viewState.copy(showOverlay = false, isLoadingData = false)
            }
            else -> {
                viewState
            }
        }
    }

    private fun actOnMainEvent(action: Action): MainViewEvent? {
        return when (action) {
            MainStateAction.WelcomeScreenNeeded -> {
                MainViewEvent.WelcomeScreenNeeded
            }
            else -> {
                null
            }
        }
    }

    sealed class MainStateAction : Action {
        object LoadingStarted : MainStateAction()
        class LoadError(val message: String) : MainStateAction()
        object WelcomeScreenNeeded : MainStateAction()
        class LoadedUserId(val userId: String) : MainStateAction()
        class LoadedPartners(val partnerIds: List<String>) : MainStateAction()
        class LoadedSexFilter(val sex: Sex?) : MainStateAction()
        class LoadedUnfilteredNames(val count: Int) : MainStateAction()
        class LoadedStarredNames(val count: Int) : MainStateAction()
        object LoadingFinished : MainStateAction()
    }
}

sealed class MainViewEvent : ViewEvent {
    object WelcomeScreenNeeded : MainViewEvent()
}

data class MainViewState(
    val isLoadingData: Boolean = false,
    val showOverlay: Boolean = true,
    val error: String? = null,
    val isLoggedIn: Boolean = false,
    val partnersCount: Int = 0,
    val sexFilterLoaded: Boolean = false,
    val sexFilter: Sex? = null,
    val unfilteredNamesCount: Int = 0,
    val starredNamesCount: Int = 0
) : ViewState