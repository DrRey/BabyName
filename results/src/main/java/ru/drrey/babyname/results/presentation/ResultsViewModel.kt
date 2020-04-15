package ru.drrey.babyname.results.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.drrey.babyname.common.presentation.base.InteractorObserver
import ru.drrey.babyname.results.domain.entity.Result
import ru.drrey.babyname.results.domain.interactor.GetResultsInteractor

class ResultsViewModel(
    private val getResultsInteractor: GetResultsInteractor
) : ViewModel() {
    private val state: MutableLiveData<ResultsState> by lazy {
        MutableLiveData<ResultsState>().apply {
            value = ResultsNotLoaded
        }
    }

    override fun onCleared() {
        getResultsInteractor.dispose()
        super.onCleared()
    }

    fun getState(): LiveData<ResultsState> = state

    fun loadResults() {
        state.value = ResultsLoading
        getResultsInteractor.execute(null, InteractorObserver<List<Result>>()
            .onError {
                state.value = ResultsLoadError(it, it.message)
            }
            .onNext {
                state.value = ResultsLoaded(it)
            })
    }
}