package ru.drrey.babyname.partners.domain.interactor

import io.reactivex.Observable
import io.reactivex.Single
import ru.drrey.babyname.common.domain.executor.PostExecutionThread
import ru.drrey.babyname.common.domain.executor.ThreadExecutor
import ru.drrey.babyname.common.domain.interactor.base.BaseInteractor
import ru.drrey.babyname.partners.domain.entity.Partner
import ru.drrey.babyname.partners.domain.repository.PartnersRepository

/**
 * Get partners list interactor
 */
class GetPartnersListInteractor(
    private val partnersRepository: PartnersRepository,
    private val getUserId: () -> Single<String>,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : BaseInteractor<List<Partner>, Void?>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<List<Partner>> {
        return getUserId().flatMap { userId ->
            partnersRepository.getPartnersList(userId)
        }.toObservable()
    }
}