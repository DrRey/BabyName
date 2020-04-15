package ru.drrey.babyname.results.domain.interactor

import io.reactivex.Observable
import io.reactivex.Single
import ru.drrey.babyname.common.domain.entity.NameStars
import ru.drrey.babyname.common.domain.executor.PostExecutionThread
import ru.drrey.babyname.common.domain.executor.ThreadExecutor
import ru.drrey.babyname.common.domain.interactor.base.BaseInteractor
import ru.drrey.babyname.results.domain.entity.Result

/**
 * Get results interactor
 */
class GetResultsInteractor(
    private val getUserId: () -> Single<String>,
    private val getStars: (String) -> Single<List<NameStars>>,
    private val getPartners: (String) -> Single<List<String>>,
    private val getPartnersStars: (List<String>) -> Single<Map<String, List<NameStars>>>,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : BaseInteractor<List<Result>, Void?>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<List<Result>> {
        return getUserId().flatMap { userId ->
            getStars(userId).flatMap { stars ->
                getPartners(userId).onErrorReturn { emptyList() }.flatMap { partners ->
                    getPartnersStars(partners).onErrorReturn { emptyMap() }
                        .flatMap { partnersStars ->
                            Single.just(
                                partnersStars //partner stars map
                                    .plus(
                                        Pair(
                                            userId,
                                            stars
                                        )
                                    ) //adding user stars to map -> Map<String, List<NameStars>>
                                    .values.asSequence() //getting just the values sequence -> Collection<List<NameStars>>
                                    .flatten() //getting the whole list of stars -> List<NameStars>
                                    .groupBy(  //grouping by name and stars -> Map<String, List<Int>>
                                        { nameStars -> nameStars.name },
                                        { nameStars ->
                                            nameStars.stars ?: 0
                                        })
                                    .mapValues { it.value.average() } //mapping values to average -> Map<String, Double>
                                    .map {
                                        Result(
                                            it.key,
                                            it.value.toFloat()
                                        )
                                    } //mapping to results list -> List<Results>
                                    .sortedByDescending { it.averageStars } //sorting
                            )
                        }
                }
            }
        }.toObservable()
    }
}