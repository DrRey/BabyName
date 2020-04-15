package ru.drrey.babyname.auth.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.drrey.babyname.auth.domain.entity.NotLoggedInException
import ru.drrey.babyname.auth.domain.interactor.GetUserIdInteractor
import ru.drrey.babyname.auth.domain.interactor.SetUserIdInteractor
import ru.drrey.babyname.common.presentation.base.InteractorObserver

class AuthViewModel(
    private val getUserIdInteractor: GetUserIdInteractor,
    private val setUserIdInteractor: SetUserIdInteractor
) : ViewModel() {
    private val state: MutableLiveData<AuthState> by lazy {
        MutableLiveData<AuthState>().apply {
            value = AuthUndefined
        }
    }

    fun getState(): LiveData<AuthState> = state

    fun loadAuth() {
        state.value = AuthLoading
        getUserIdInteractor.execute(null, InteractorObserver<String>()
            .onError {
                if (it is NotLoggedInException) {
                    state.value = AuthNone
                } else {
                    state.value = AuthError(it, it.message)
                }
            }
            .onNext {
                state.value = AuthComplete(it)
            })
    }

    fun onAuthComplete(userId: String) {
        setUserIdInteractor.execute(userId, InteractorObserver<Void>()
            .onError {
                state.value = AuthError(it, it.message)
            }
            .onComplete {
                state.value = AuthComplete(userId)
            })
    }

    fun onAuthError(t: Throwable?) {
        state.value = AuthError(t, t?.message)
    }
}