package ru.drrey.babyname.names.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.drrey.babyname.common.presentation.base.*
import ru.drrey.babyname.names.domain.entity.Name
import ru.drrey.babyname.names.domain.interactor.GetUnfilteredNamesInteractor
import ru.drrey.babyname.names.domain.interactor.SetNameFilterInteractor

class FilterViewModel(
    private val getUnfilteredNamesInteractor: GetUnfilteredNamesInteractor,
    private val setNameFilterInteractor: SetNameFilterInteractor
) : ViewModel(), StateViewModel<FilterViewState, FilterViewEvent> {

    override val viewState by lazy {
        MutableLiveData<FilterViewState>().apply {
            value = initialViewState
        }
    }
    override val viewEvent by lazy { LiveEvent<FilterViewEvent>() }

    override val initialViewState = FilterViewState()
    override val stateReducers = listOf<Reducer<FilterViewState>>(::reduceFilterViewState)
    override val eventActors = listOf<Actor<FilterViewEvent>>(::actOnFilterEvent)

    private var names = emptyList<Name>()

    fun loadNames() {
        getUnfilteredNamesInteractor.execute(viewModelScope, null) {
            names = it
            loadNextName()
        }
    }

    fun onNameFiltered(allow: Boolean) {
        val name = names.first()
        names = names.minus(name)
        setNameFilterInteractor.execute(viewModelScope, SetNameFilterInteractor.Params(name, allow))
        viewModelScope.launch {
            act(FilterStateAction.NameLoading)
            delay(500L)
            loadNextName()
        }
    }

    private fun loadNextName() {
        if (names.isNotEmpty()) {
            act(FilterStateAction.NameLoaded(names.first()))
        } else {
            act(FilterStateAction.NoNamesLeft)
        }
    }

    private fun reduceFilterViewState(viewState: FilterViewState, action: Action): FilterViewState {
        return when (action) {
            FilterStateAction.NameLoading -> {
                viewState.copy(currentName = null)
            }
            is FilterStateAction.NameLoaded -> {
                viewState.copy(currentName = action.name)
            }
            else -> {
                viewState
            }
        }
    }

    private fun actOnFilterEvent(action: Action): FilterViewEvent? {
        return when (action) {
            is FilterStateAction.Error -> {
                FilterViewEvent.Error(action.error)
            }
            is FilterStateAction.NoNamesLeft -> {
                FilterViewEvent.NoNamesLeft
            }
            else -> {
                null
            }
        }
    }

    sealed class FilterStateAction : Action {
        object NameLoading : FilterStateAction()
        class NameLoaded(val name: Name) : FilterStateAction()
        class Error(val error: String) : FilterStateAction()
        object NoNamesLeft : FilterStateAction()
    }
}

sealed class FilterViewEvent : ViewEvent {
    class Error(val error: String) : FilterViewEvent()
    object NoNamesLeft : FilterViewEvent()
}

data class FilterViewState(val currentName: Name? = null) : ViewState