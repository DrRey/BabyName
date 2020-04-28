@file:Suppress("EXPERIMENTAL_API_USAGE")

package ru.drrey.babyname.auth.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import ru.drrey.babyname.auth.domain.entity.NotLoggedInException
import ru.drrey.babyname.auth.domain.interactor.GetUserIdInteractor
import ru.drrey.babyname.auth.domain.interactor.SetUserIdInteractor
import ru.drrey.babyname.common.presentation.base.*

class AuthViewModel(
    private val getUserIdInteractor: GetUserIdInteractor,
    private val setUserIdInteractor: SetUserIdInteractor
) : ViewModel(), StateViewModel<AuthViewState, AuthViewEvent> {

    override val viewState by lazy {
        MutableLiveData<AuthViewState>().apply {
            value = initialViewState
        }
    }
    override val viewEvent by lazy { LiveEvent<AuthViewEvent>() }

    override val initialViewState = AuthViewState()
    override val stateReducers = listOf<Reducer<AuthViewState>>(::reduceAuthViewState)
    override val eventActors = listOf<Actor<AuthViewEvent>>(::actOnAuthEvent)

    fun loadAuth() {
        act(AuthStateAction.AuthLoadStarted)
        getUserIdInteractor.execute(viewModelScope, null, onError = {
            if (it is NotLoggedInException) {
                act(AuthStateAction.AuthNone)
            } else {
                act(AuthStateAction.AuthError(it.message ?: "Auth error"))
            }
        }) {
            act(AuthStateAction.AuthComplete(it))
        }
    }

    fun onAuthComplete(userId: String) {
        setUserIdInteractor.execute(
            viewModelScope,
            userId,
            onSuccess = { act(AuthStateAction.AuthComplete(userId)) })
    }

    fun onAuthError(t: Throwable?) {
        act(AuthStateAction.AuthError(t?.message ?: ""))
    }

    private fun reduceAuthViewState(viewState: AuthViewState, action: Action): AuthViewState {
        return when (action) {
            AuthStateAction.AuthLoadStarted -> {
                viewState.copy(isLoaded = false, isLoading = true, error = null, userId = null)
            }
            is AuthStateAction.AuthError -> {
                viewState.copy(
                    isLoaded = true,
                    isLoading = false,
                    error = action.message,
                    userId = null
                )
            }
            is AuthStateAction.AuthComplete -> {
                viewState.copy(
                    isLoaded = true,
                    isLoading = false,
                    error = null,
                    userId = action.userId
                )
            }
            AuthStateAction.AuthNone -> {
                viewState.copy(isLoaded = true, isLoading = false, error = null, userId = null)
            }
            else -> {
                viewState
            }
        }
    }

    private fun actOnAuthEvent(action: Action): AuthViewEvent? {
        return null
    }

    sealed class AuthStateAction : Action {
        object AuthLoadStarted : AuthStateAction()
        class AuthError(val message: String) : AuthStateAction()
        class AuthComplete(val userId: String) : AuthStateAction()
        object AuthNone : AuthStateAction()
    }
}

sealed class AuthViewEvent : ViewEvent

data class AuthViewState(
    val isLoaded: Boolean = false,
    val isLoading: Boolean = false,
    val userId: String? = null,
    val error: String? = null
) : ViewState