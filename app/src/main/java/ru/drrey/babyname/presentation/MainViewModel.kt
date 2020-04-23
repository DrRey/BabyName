package ru.drrey.babyname.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import ru.drrey.babyname.common.domain.interactor.base.BaseInteractor
import ru.drrey.babyname.common.presentation.base.*

class MainViewModel(
    private val getUserIdInteractor: BaseInteractor<String, Void?>,
    private val getPartnerIdsListInteractor: BaseInteractor<List<String>, Void?>,
    private val clearPartnersInteractor: BaseInteractor<Nothing, Void?>,
    private val getStarredNamesInteractor: BaseInteractor<Int, Void?>
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
            is MainStateAction.LoadedUserId -> {
                viewState.copy(userId = action.userId)
            }
            is MainStateAction.LoadedPartners -> {
                viewState.copy(partnerIds = action.partnerIds)
            }
            is MainStateAction.LoadedStarredNames -> {
                viewState.copy(starredNamesCount = action.count)
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
        class LoadedUserId(val userId: String) : MainStateAction()
        class LoadedPartners(val partnerIds: List<String>) : MainStateAction()
        class LoadedStarredNames(val count: Int) : MainStateAction()
    }
}

sealed class MainViewEvent : ViewEvent

data class MainViewState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val userId: String? = null,
    val partnerIds: List<String>? = null,
    val starredNamesCount: Int? = null
) : ViewState