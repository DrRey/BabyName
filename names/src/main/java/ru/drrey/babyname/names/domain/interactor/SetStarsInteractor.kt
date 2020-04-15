package ru.drrey.babyname.names.domain.interactor

import io.reactivex.Observable
import io.reactivex.Single
import ru.drrey.babyname.common.domain.executor.PostExecutionThread
import ru.drrey.babyname.common.domain.executor.ThreadExecutor
import ru.drrey.babyname.common.domain.interactor.base.BaseInteractor
import ru.drrey.babyname.names.domain.entity.Name
import ru.drrey.babyname.names.domain.repository.NamesRepository

/**
 * Set stars interactor
 */
class SetStarsInteractor(
    private val namesRepository: NamesRepository,
    private val getUserId: () -> Single<String>,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : BaseInteractor<Void, SetStarsInteractor.Params>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Params): Observable<Void> {
        return getUserId().flatMapCompletable { userId ->
            namesRepository.setStars(userId, params.name, params.stars)
        }.toObservable()
    }

    class Params(val name: Name, val stars: Int)
}