package ru.drrey.babyname.auth.domain.interactor

import io.reactivex.Observable
import ru.drrey.babyname.auth.domain.repository.AuthRepository
import ru.drrey.babyname.common.domain.executor.PostExecutionThread
import ru.drrey.babyname.common.domain.executor.ThreadExecutor
import ru.drrey.babyname.common.domain.interactor.base.BaseInteractor

/**
 * Get user id interactor
 */
class GetUserIdInteractor(
    private val authRepository: AuthRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : BaseInteractor<String, Void?>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<String> {
        return authRepository.getUserId().toObservable()
    }
}