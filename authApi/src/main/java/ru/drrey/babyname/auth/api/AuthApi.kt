package ru.drrey.babyname.auth.api

import kotlinx.coroutines.flow.Flow
import ru.drrey.babyname.common.domain.interactor.base.BaseInteractor
import ru.drrey.babyname.navigationmediator.AuthFlowScreenProvider

interface AuthApi {
    fun getUserIdInteractor(): BaseInteractor<String, Void?>
    fun getUserId(): Flow<String>
    fun getFlowScreenProvider(): AuthFlowScreenProvider
}