package ru.drrey.babyname.common.presentation.base

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hadilq.liveevent.LiveEvent

/**
 * Interfaces for MVI realization. Used in VMs
 */
interface StateViewModel<T : ViewState, Z : ViewEvent> {
    val initialViewState: T

    val viewState: MutableLiveData<T>
    val viewEvent: LiveEvent<Z>

    val stateReducers: List<Reducer<T>>
    val eventActors: List<Actor<Z>>

    fun getViewState(): LiveData<T> = viewState
    fun getViewEvent(): LiveData<Z> = viewEvent

    fun act(action: Action) {
        eventActors.forEach { actor ->
            actor(action)?.let {
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    viewEvent.value = it
                } else {
                    viewEvent.postValue(it)
                }
            }
        }

        val newState = reduce(viewState.value ?: initialViewState, action)
        if (newState != viewState.value) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                viewState.value = newState
            } else {
                viewState.postValue(newState)
            }
        }
    }

    private fun reduce(currentState: T, action: Action): T {
        var newState = currentState
        stateReducers.forEach { reducer ->
            newState = reducer(newState, action)
        }
        return newState
    }
}

interface Action

interface ViewState

interface ViewEvent

typealias Reducer<T> = (T, Action) -> T
typealias Actor<Z> = (Action) -> Z?