package ru.drrey.babyname.auth.domain.interactor

import io.reactivex.Observable
import ru.drrey.babyname.auth.domain.repository.AuthRepository
import ru.drrey.babyname.common.domain.executor.PostExecutionThread
import ru.drrey.babyname.common.domain.executor.ThreadExecutor
import ru.drrey.babyname.common.domain.interactor.base.BaseInteractor

/**
 * Set user id interactor
 */
class SetUserIdInteractor(
    private val authRepository: AuthRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : BaseInteractor<Void, String>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: String): Observable<Void> {
        return authRepository.setUserId(params).toObservable()
    }
}