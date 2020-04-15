package ru.drrey.babyname.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.drrey.babyname.common.domain.interactor.base.BaseInteractor
import ru.drrey.babyname.common.presentation.base.InteractorObserver

class MainViewModel(
    private val getUserIdInteractor: BaseInteractor<String, Void?>,
    private val getPartnerIdsListInteractor: BaseInteractor<List<String>, Void?>,
    private val clearPartnersInteractor: BaseInteractor<Void, Void?>,
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
        getUserIdInteractor.execute(null, InteractorObserver<String>()
            .onError {
                state.value = LoadError(null, null, null, it, it.message)
            }
            .onNext { userId ->
                getPartnerIdsListInteractor.execute(null, InteractorObserver<List<String>>()
                    .onError {
                        state.value = LoadError(userId, null, null, it, it.message)
                    }
                    .onNext { partnerIds ->
                        getStarredNamesInteractor.execute(null, InteractorObserver<Int>()
                            .onError {
                                state.value = LoadError(userId, partnerIds, null, it, it.message)
                            }
                            .onNext {
                                state.value = Loaded(userId, partnerIds, it)
                            })
                    })
            })
    }

    fun onClearPartners() {
        clearPartnersInteractor.execute(null, InteractorObserver<Void>()
            .onError {

            }
            .onComplete {
                loadData()
            })
    }
}