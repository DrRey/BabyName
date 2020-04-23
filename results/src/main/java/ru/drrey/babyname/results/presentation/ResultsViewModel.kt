package ru.drrey.babyname.results.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import ru.drrey.babyname.common.presentation.base.*
import ru.drrey.babyname.results.domain.entity.Result
import ru.drrey.babyname.results.domain.interactor.GetResultsInteractor

class ResultsViewModel(
    private val getResultsInteractor: GetResultsInteractor
) : ViewModel(), StateViewModel<ResultsViewState, ResultsViewEvent> {

    override val viewState by lazy {
        MutableLiveData<ResultsViewState>().apply {
            value = initialViewState
        }
    }
    override val viewEvent by lazy { LiveEvent<ResultsViewEvent>() }

    override val initialViewState = ResultsViewState()
    override val stateReducers = listOf<Reducer<ResultsViewState>>(::reduceResultsViewState)
    override val eventActors = listOf<Actor<ResultsViewEvent>>(::actOnResultsEvent)

    fun loadResults() {
        act(ResultsStateAction.ResultsLoadStarted)
        getResultsInteractor.execute(
            viewModelScope,
            null,
            onError = { act(ResultsStateAction.ResultsError(it.message ?: "")) })
        {
            act(ResultsStateAction.ResultsLoaded(it))
        }
    }

    private fun reduceResultsViewState(
        viewState: ResultsViewState,
        action: Action
    ): ResultsViewState {
        return when (action) {
            ResultsStateAction.ResultsLoadStarted -> {
                viewState.copy(isLoaded = false, isLoading = true, results = null, error = null)
            }
            is ResultsStateAction.ResultsLoaded -> {
                viewState.copy(
                    isLoaded = true,
                    isLoading = false,
                    results = action.results,
                    error = null
                )
            }
            is ResultsStateAction.ResultsError -> {
                viewState.copy(
                    isLoaded = true,
                    isLoading = false,
                    results = null,
                    error = action.message
                )
            }
            else -> {
                viewState
            }
        }
    }

    private fun actOnResultsEvent(action: Action): ResultsViewEvent? {
        return null
    }

    sealed class ResultsStateAction : Action {
        object ResultsLoadStarted : ResultsStateAction()
        class ResultsError(val message: String) : ResultsStateAction()
        class ResultsLoaded(val results: List<Result>) : ResultsStateAction()
    }
}

sealed class ResultsViewEvent : ViewEvent

data class ResultsViewState(
    val isLoaded: Boolean = false,
    val isLoading: Boolean = false,
    val results: List<Result>? = null,
    val error: String? = null
) : ViewState