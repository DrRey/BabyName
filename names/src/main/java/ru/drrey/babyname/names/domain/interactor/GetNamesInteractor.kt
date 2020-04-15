package ru.drrey.babyname.names.domain.interactor

import io.reactivex.Observable
import ru.drrey.babyname.common.domain.executor.PostExecutionThread
import ru.drrey.babyname.common.domain.executor.ThreadExecutor
import ru.drrey.babyname.common.domain.interactor.base.BaseInteractor
import ru.drrey.babyname.names.domain.entity.Name
import ru.drrey.babyname.names.domain.repository.NamesRepository

/**
 * Get names interactor
 */
class GetNamesInteractor(
    private val namesRepository: NamesRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : BaseInteractor<List<Name>, Void?>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<List<Name>> {
        return namesRepository.getNames()
            .map { it.sortedWith(compareBy({ name -> name.sex }, { name -> name.displayName })) }
            .toObservable()
    }
}