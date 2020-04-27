package ru.drrey.babyname.auth.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.drrey.babyname.auth.domain.repository.AuthRepository
import ru.drrey.babyname.common.domain.interactor.base.Interactor

/**
 * Get user id interactor
 */
class GetUserIdInteractor(
    private val authRepository: AuthRepository
) : Interactor<String, Void?>() {

    override fun buildFlow(params: Void?): Flow<String> {
        return authRepository.getUserId()
    }
}