package ru.drrey.babyname.names.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.drrey.babyname.common.presentation.base.InteractorObserver
import ru.drrey.babyname.names.domain.entity.Name
import ru.drrey.babyname.names.domain.interactor.GetNamesWithStarsInteractor
import ru.drrey.babyname.names.domain.interactor.SetStarsInteractor

class NamesViewModel(
    private val getNamesWithStarsInteractor: GetNamesWithStarsInteractor,
    private val setStarsInteractor: SetStarsInteractor
) : ViewModel() {
    private val state: MutableLiveData<NamesState> by lazy {
        MutableLiveData<NamesState>().apply {
            value = NamesNotLoaded
        }
    }

    override fun onCleared() {
        getNamesWithStarsInteractor.dispose()
        setStarsInteractor.dispose()
        super.onCleared()
    }

    fun getState(): LiveData<NamesState> = state

    fun loadNames() {
        state.value = NamesLoading
        getNamesWithStarsInteractor.execute(null, InteractorObserver<List<Name>>()
            .onError {
                state.value = NamesLoadError(it, it.message)
            }
            .onNext {
                state.value = NamesLoaded(it)
            })
    }

    fun setStars(name: Name, position: Int, stars: Int) {
        setStarsInteractor.execute(SetStarsInteractor.Params(name, stars),
            InteractorObserver<Void>()
                .onError {
                    state.value = SetStarsError(name, position, it, it.message)
                }
                .onComplete {
                    state.value = SetStarsSuccess(name, position, stars)
                })
    }
}