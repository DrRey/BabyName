package ru.drrey.babyname.auth.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.drrey.babyname.auth.domain.repository.AuthRepository
import ru.drrey.babyname.common.domain.interactor.base.BaseInteractor

/**
 * Get user id interactor
 */
class GetUserIdInteractor(
    private val authRepository: AuthRepository
) : BaseInteractor<String, Void?>() {

    override fun buildFlow(params: Void?): Flow<String> {
        return authRepository.getUserId()
    }
}