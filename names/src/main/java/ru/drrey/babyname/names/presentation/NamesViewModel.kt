package ru.drrey.babyname.names.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.drrey.babyname.names.domain.entity.Name
import ru.drrey.babyname.names.domain.interactor.GetNamesWithStarsInteractor
import ru.drrey.babyname.names.domain.interactor.SetStarsInteractor

@ExperimentalCoroutinesApi
class NamesViewModel(
    private val getNamesWithStarsInteractor: GetNamesWithStarsInteractor,
    private val setStarsInteractor: SetStarsInteractor
) : ViewModel() {
    private val state: MutableLiveData<NamesState> by lazy {
        MutableLiveData<NamesState>().apply {
            value = NamesNotLoaded
        }
    }

    fun getState(): LiveData<NamesState> = state

    fun loadNames() {
        state.value = NamesLoading
        getNamesWithStarsInteractor.execute(
            viewModelScope,
            null,
            onError = { state.value = NamesLoadError(it, it.message) }) {
            state.value = NamesLoaded(it)
        }
    }

    fun setStars(name: Name, position: Int, stars: Int) {
        setStarsInteractor.execute(
            viewModelScope,
            SetStarsInteractor.Params(name, stars),
            onError = {
                state.value = SetStarsError(name, position, it, it.message)
            }) { state.value = SetStarsSuccess(name, position, stars) }
    }
}