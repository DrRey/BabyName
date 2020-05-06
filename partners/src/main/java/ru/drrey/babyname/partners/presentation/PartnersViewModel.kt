package ru.drrey.babyname.partners.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import ru.drrey.babyname.common.domain.interactor.base.Interactor
import ru.drrey.babyname.common.presentation.base.*
import ru.drrey.babyname.partners.domain.interactor.AddPartnerInteractor

class PartnersViewModel(
    private val getUserIdInteractor: Interactor<String, Nothing?>,
    private val addPartnerInteractor: AddPartnerInteractor
) : ViewModel(), StateViewModel<PartnersViewState, PartnersViewEvent> {

    override val viewState by lazy {
        MutableLiveData<PartnersViewState>().apply {
            value = initialViewState
        }
    }
    override val viewEvent by lazy { LiveEvent<PartnersViewEvent>() }

    override val initialViewState = PartnersViewState()
    override val stateReducers = listOf<Reducer<PartnersViewState>>(::reducePartnersViewState)
    override val eventActors = listOf<Actor<PartnersViewEvent>>(::actOnPartnersEvent)

    fun loadUserData() {
        getUserIdInteractor.execute(
            viewModelScope,
            null,
            onError = { act(PartnersStateAction.UserLoadError(it.message ?: "")) })
        {
            act(PartnersStateAction.UserLoaded(it))
        }
    }

    fun onAddPartner(partnerId: String) {
        addPartnerInteractor.execute(
            viewModelScope,
            partnerId,
            onError = { act(PartnersStateAction.PartnerAddError(it.message ?: "")) }) {
            act(PartnersStateAction.PartnerAdded)
        }
    }

    private fun reducePartnersViewState(
        viewState: PartnersViewState,
        action: Action
    ): PartnersViewState {
        return when (action) {
            is PartnersStateAction.UserLoaded -> {
                viewState.copy(userId = action.userId, loadUserError = null)
            }
            is PartnersStateAction.UserLoadError -> {
                viewState.copy(userId = null, loadUserError = action.message)
            }
            else -> {
                viewState
            }
        }
    }

    private fun actOnPartnersEvent(action: Action): PartnersViewEvent? {
        return when (action) {
            PartnersStateAction.PartnerAdded -> {
                PartnersViewEvent.PartnerAdded
            }
            is PartnersStateAction.PartnerAddError -> {
                PartnersViewEvent.PartnerAddError(action.message)
            }
            else -> {
                null
            }
        }
    }

    sealed class PartnersStateAction : Action {
        class UserLoaded(val userId: String) : PartnersStateAction()
        class UserLoadError(val message: String) : PartnersStateAction()
        object PartnerAdded : PartnersStateAction()
        class PartnerAddError(val message: String) : PartnersStateAction()
    }
}

sealed class PartnersViewEvent : ViewEvent {
    object PartnerAdded : PartnersViewEvent()
    class PartnerAddError(val message: String) : PartnersViewEvent()
}

data class PartnersViewState(
    val userId: String? = null,
    val loadUserError: String? = null
) : ViewState