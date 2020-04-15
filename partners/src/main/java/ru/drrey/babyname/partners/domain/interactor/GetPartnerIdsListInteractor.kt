package ru.drrey.babyname.partners.domain.interactor

import io.reactivex.Observable
import io.reactivex.Single
import ru.drrey.babyname.common.domain.executor.PostExecutionThread
import ru.drrey.babyname.common.domain.executor.ThreadExecutor
import ru.drrey.babyname.common.domain.interactor.base.BaseInteractor
import ru.drrey.babyname.partners.domain.repository.PartnersRepository

/**
 * Get partner ids list interactor
 */
class GetPartnerIdsListInteractor(
    private val partnersRepository: PartnersRepository,
    private val getUserId: () -> Single<String>,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : BaseInteractor<List<String>, Void?>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<List<String>> {
        return getUserId().flatMap { userId ->
            partnersRepository.getPartnersList(userId).map { it.map { partner -> partner.id } }
        }.toObservable()
    }
}