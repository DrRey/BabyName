package ru.drrey.babyname.names.domain.interactor

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import ru.drrey.babyname.common.domain.entity.NameStars
import ru.drrey.babyname.common.domain.executor.PostExecutionThread
import ru.drrey.babyname.common.domain.executor.ThreadExecutor
import ru.drrey.babyname.common.domain.interactor.base.BaseInteractor
import ru.drrey.babyname.names.domain.entity.Name
import ru.drrey.babyname.names.domain.repository.NamesRepository

/**
 * Get names with stars interactor
 */
class GetNamesWithStarsInteractor(
    private val namesRepository: NamesRepository,
    private val getUserId: () -> Single<String>,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : BaseInteractor<List<Name>, Void?>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<List<Name>> {
        return getUserId().flatMap { userId ->
            namesRepository.getNames()
                .zipWith(
                    namesRepository.getStars(userId),
                    BiFunction<List<Name>, List<NameStars>, List<Name>> { namesList, starsList ->
                        return@BiFunction namesList.asSequence().map { name ->
                            name.apply {
                                stars =
                                    starsList.firstOrNull { star -> star.name == name.displayName }?.stars
                            }
                        }.sortedWith(compareBy({ it.sex }, { it.displayName })).toList()
                    })
        }.toObservable()
    }
}