package ru.drrey.babyname.names.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
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

    private val namesList by lazy { MutableLiveData<List<Name>>() }
    private val namesMap = mutableMapOf<Name, Boolean?>()

    fun loadNames() {
        getUnfilteredNamesInteractor.execute(viewModelScope, null) {
            namesList.value = it
            namesMap.clear()
            loadNextName()
        }
    }

    fun onNameFiltered(name: Name, allow: Boolean) {
        namesMap[name] = allow
        setNameFilterInteractor.execute(viewModelScope, SetNameFilterInteractor.Params(name, allow))
        loadNextName()
    }

    private fun loadNextName() {
        namesList.value?.indexOfFirst { !namesMap.contains(it) }?.let {
            namesList.value?.getOrNull(it)?.let { name ->
                act(FilterStateAction.NameLoaded(name, it))
            }
        } ?: run {
            act(FilterStateAction.NoNamesLeft)
        }
    }

    fun getNamesList(): LiveData<List<Name>> = namesList
    fun getNamesMap(): Map<Name, Boolean?> = namesMap

    private fun reduceFilterViewState(viewState: FilterViewState, action: Action): FilterViewState {
        return when (action) {
            FilterStateAction.NameLoading -> {
                viewState.copy(currentName = null, currentNamePosition = null)
            }
            is FilterStateAction.NameLoaded -> {
                viewState.copy(currentName = action.name, currentNamePosition = action.position)
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
        class NameLoaded(val name: Name, val position: Int) : FilterStateAction()
        class Error(val error: String) : FilterStateAction()
        object NoNamesLeft : FilterStateAction()
    }
}

sealed class FilterViewEvent : ViewEvent {
    class Error(val error: String) : FilterViewEvent()
    object NoNamesLeft : FilterViewEvent()
}

data class FilterViewState(val currentName: Name? = null, val currentNamePosition: Int? = null) :
    ViewState