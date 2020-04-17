package ru.drrey.babyname.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.drrey.babyname.common.domain.interactor.base.BaseInteractor

@ExperimentalCoroutinesApi
class MainViewModel(
    private val getUserIdInteractor: BaseInteractor<String, Void?>,
    private val getPartnerIdsListInteractor: BaseInteractor<List<String>, Void?>,
    private val clearPartnersInteractor: BaseInteractor<Nothing, Void?>,
    private val getStarredNamesInteractor: BaseInteractor<Int, Void?>
) : ViewModel() {
    private val state: MutableLiveData<MainState> by lazy {
        MutableLiveData<MainState>().apply {
            value = NotLoaded
        }
    }

    override fun onCleared() {
        getUserIdInteractor.dispose()
        getPartnerIdsListInteractor.dispose()
        clearPartnersInteractor.dispose()
        getStarredNamesInteractor.dispose()
        super.onCleared()
    }

    fun getState(): LiveData<MainState> = state

    fun loadData() {
        state.value = Loading
        getUserIdInteractor.execute(
            viewModelScope,
            null,
            onError = { state.value = LoadError(null, null, null, it, it.message) }) { userId ->
            getPartnerIdsListInteractor.execute(
                viewModelScope,
                null,
                onError = {
                    state.value = LoadError(userId, null, null, it, it.message)
                }) { partnerIds ->
                getStarredNamesInteractor.execute(
                    viewModelScope,
                    null,
                    onError = {
                        state.value = LoadError(userId, partnerIds, null, it, it.message)
                    }) {
                    state.value = Loaded(userId, partnerIds, it)
                }
            }

        }
    }

    fun onClearPartners() {
        clearPartnersInteractor.execute(viewModelScope, null) {
            loadData()
        }
    }
}