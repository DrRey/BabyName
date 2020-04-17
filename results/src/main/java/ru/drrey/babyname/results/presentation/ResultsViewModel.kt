package ru.drrey.babyname.results.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.drrey.babyname.results.domain.interactor.GetResultsInteractor

@ExperimentalCoroutinesApi
class ResultsViewModel(
    private val getResultsInteractor: GetResultsInteractor
) : ViewModel() {
    private val state: MutableLiveData<ResultsState> by lazy {
        MutableLiveData<ResultsState>().apply {
            value = ResultsNotLoaded
        }
    }

    fun getState(): LiveData<ResultsState> = state

    fun loadResults() {
        state.value = ResultsLoading
        getResultsInteractor.execute(
            viewModelScope,
            null,
            onError = { state.value = ResultsLoadError(it, it.message) })
        {
            state.value = ResultsLoaded(it)
        }
    }
}