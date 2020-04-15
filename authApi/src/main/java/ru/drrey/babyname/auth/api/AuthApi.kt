package ru.drrey.babyname.auth.api

import io.reactivex.Single
import ru.drrey.babyname.common.domain.interactor.base.BaseInteractor
import ru.drrey.babyname.navigationmediator.AuthFlowScreenProvider

interface AuthApi {
    fun getUserIdInteractor(): BaseInteractor<String, Void?>
    fun getUserId(): Single<String>
    fun getFlowScreenProvider(): AuthFlowScreenProvider
}