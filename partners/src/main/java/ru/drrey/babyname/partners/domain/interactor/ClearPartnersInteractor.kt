package ru.drrey.babyname.partners.domain.interactor

import io.reactivex.Observable
import io.reactivex.Single
import ru.drrey.babyname.common.domain.executor.PostExecutionThread
import ru.drrey.babyname.common.domain.executor.ThreadExecutor
import ru.drrey.babyname.common.domain.interactor.base.BaseInteractor
import ru.drrey.babyname.partners.domain.repository.PartnersRepository

/**
 * Clear partners interactor
 */
class ClearPartnersInteractor(
    private val partnersRepository: PartnersRepository,
    private val getUserId: () -> Single<String>,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : BaseInteractor<Void, Void?>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<Void> {
        return getUserId().flatMapCompletable { userId ->
            partnersRepository.getPartnersList(userId).flatMapCompletable { partnerIds ->
                partnersRepository.clearPartners(userId, partnerIds)
            }
        }.toObservable()
    }
}