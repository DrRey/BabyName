package ru.drrey.babyname.auth.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.drrey.babyname.auth.domain.repository.AuthRepository
import ru.drrey.babyname.common.domain.interactor.base.BaseInteractor

/**
 * Set user id interactor
 */
class SetUserIdInteractor(
    private val authRepository: AuthRepository
) : BaseInteractor<Void, String>() {

    override fun buildFlow(params: String): Flow<Void> {
        return authRepository.setUserId(params)
    }
}