package ru.drrey.babyname.names.domain.interactor

import io.reactivex.Observable
import io.reactivex.Single
import ru.drrey.babyname.common.domain.executor.PostExecutionThread
import ru.drrey.babyname.common.domain.executor.ThreadExecutor
import ru.drrey.babyname.common.domain.interactor.base.BaseInteractor
import ru.drrey.babyname.names.domain.repository.NamesRepository

/**
 * Count names with stars interactor
 */
class CountStarredNamesInteractor(
    private val namesRepository: NamesRepository,
    private val getUserId: () -> Single<String>,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : BaseInteractor<Int, Void?>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<Int> {
        return getUserId().flatMap { userId ->
            namesRepository.getStars(userId).map { it.size }
        }.toObservable()
    }
}