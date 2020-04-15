package ru.drrey.babyname.partners.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.drrey.babyname.common.domain.interactor.base.BaseInteractor
import ru.drrey.babyname.common.presentation.base.InteractorObserver
import ru.drrey.babyname.partners.domain.interactor.AddPartnerInteractor

class PartnersViewModel(
    private val getUserIdInteractor: BaseInteractor<String, Void?>,
    private val addPartnerInteractor: AddPartnerInteractor
) : ViewModel() {
    private val state: MutableLiveData<PartnersState> by lazy {
        MutableLiveData<PartnersState>().apply {
            value = Initial
        }
    }

    override fun onCleared() {
        getUserIdInteractor.dispose()
        addPartnerInteractor.dispose()
        super.onCleared()
    }

    fun getState(): LiveData<PartnersState> = state

    fun loadUserData() {
        getUserIdInteractor.execute(null, InteractorObserver<String>()
            .onError {
                state.value = GetUserError(it, it.message)
            }
            .onNext {
                state.value = GetUserSuccess(it)
            })
    }

    fun onAddPartner(partnerId: String) {
        addPartnerInteractor.execute(partnerId, InteractorObserver<Void>()
            .onError {
                state.value = PartnerAddError(it, it.message)
            }
            .onComplete {
                state.value = PartnerAddSuccess
            })
    }
}