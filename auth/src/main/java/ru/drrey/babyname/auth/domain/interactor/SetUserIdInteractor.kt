package ru.drrey.babyname.auth.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.drrey.babyname.auth.domain.repository.AuthRepository
import ru.drrey.babyname.common.domain.interactor.base.Interactor

/**
 * Set user id interactor
 */
class SetUserIdInteractor(
    private val authRepository: AuthRepository
) : Interactor<Void, String>() {

    override fun buildFlow(params: String): Flow<Void> {
        return authRepository.setUserId(params)
    }
}