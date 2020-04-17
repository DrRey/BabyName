package ru.drrey.babyname.names.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.drrey.babyname.common.presentation.base.*
import ru.drrey.babyname.names.domain.entity.Name
import ru.drrey.babyname.names.domain.interactor.GetNamesWithStarsInteractor
import ru.drrey.babyname.names.domain.interactor.SetStarsInteractor

@ExperimentalCoroutinesApi
class NamesViewModel(
    private val getNamesWithStarsInteractor: GetNamesWithStarsInteractor,
    private val setStarsInteractor: SetStarsInteractor
) : ViewModel(), StateViewModel<NamesViewState, NamesViewEvent> {

    override val viewState by lazy {
        MutableLiveData<NamesViewState>().apply {
            value = initialViewState
        }
    }
    override val viewEvent by lazy { LiveEvent<NamesViewEvent>() }

    override val initialViewState = NamesViewState()
    override val stateReducers = listOf<Reducer<NamesViewState>>(::reduceNamesViewState)
    override val eventActors = listOf<Actor<NamesViewEvent>>(::actOnNamesEvent)

    fun loadNames() {
        act(NamesStateAction.NamesLoading)
        getNamesWithStarsInteractor.execute(
            viewModelScope,
            null,
            onError = { act(NamesStateAction.NamesLoadError(it.message ?: "")) }) {
            act(NamesStateAction.NamesLoaded(it))
        }
    }

    fun setStars(name: Name, position: Int, stars: Int) {
        setStarsInteractor.execute(
            viewModelScope,
            SetStarsInteractor.Params(name, stars),
            onError = {
                act(NamesStateAction.StarsSetError(it.message ?: ""))
            }) { act(NamesStateAction.StarsSet(name, position, stars)) }
    }

    private fun reduceNamesViewState(viewState: NamesViewState, action: Action): NamesViewState {
        return when (action) {
            NamesStateAction.NamesLoading -> {
                viewState.copy(isLoaded = false, isLoading = true, loadError = null, names = null)
            }
            is NamesStateAction.NamesLoaded -> {
                viewState.copy(
                    isLoaded = true,
                    isLoading = false,
                    loadError = null,
                    names = action.names
                )
            }
            is NamesStateAction.NamesLoadError -> {
                viewState.copy(
                    isLoaded = true,
                    isLoading = false,
                    loadError = action.message,
                    names = null
                )
            }
            is NamesStateAction.StarsSet -> {
                viewState.copy(names = viewState.names?.apply {
                    getOrNull(action.position)?.stars = action.stars
                })
            }
            else -> {
                viewState
            }
        }
    }

    private fun actOnNamesEvent(action: Action): NamesViewEvent? {
        return when (action) {
            is NamesStateAction.StarsSetError -> {
                NamesViewEvent.StarsSetError(action.error)
            }
            else -> {
                null
            }
        }
    }

    sealed class NamesStateAction : Action {
        object NamesLoading : NamesStateAction()
        class NamesLoaded(val names: List<Name>) : NamesStateAction()
        class NamesLoadError(val message: String) : NamesStateAction()
        class StarsSet(val name: Name, val position: Int, val stars: Int) : NamesStateAction()
        class StarsSetError(val error: String) : NamesStateAction()
    }
}

sealed class NamesViewEvent : ViewEvent {
    class StarsSetError(val error: String) : NamesViewEvent()
}

data class NamesViewState(
    val isLoaded: Boolean = false,
    val isLoading: Boolean = false,
    val loadError: String? = null,
    val names: List<Name>? = null
) : ViewState