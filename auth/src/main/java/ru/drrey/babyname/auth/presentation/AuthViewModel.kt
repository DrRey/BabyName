@file:Suppress("EXPERIMENTAL_API_USAGE")

package ru.drrey.babyname.auth.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.drrey.babyname.auth.domain.interactor.GetUserIdInteractor
import ru.drrey.babyname.auth.domain.interactor.SetUserIdInteractor

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
        getUserIdInteractor.execute(viewModelScope, null, onError = {}) {
            state.value = AuthComplete(it)
        }
    }

    fun onAuthComplete(userId: String) {
        setUserIdInteractor.execute(viewModelScope, userId, onError = {}) {
            state.value = AuthComplete(userId)
        }
    }

    fun onAuthError(t: Throwable?) {
        state.value = AuthError(t, t?.message)
    }
}