package ru.drrey.babyname.results.domain.interactor

import kotlinx.coroutines.flow.*
import ru.drrey.babyname.common.domain.entity.NameStars
import ru.drrey.babyname.common.domain.interactor.base.BaseInteractor
import ru.drrey.babyname.results.domain.entity.Result

/**
 * Get results interactor
 */
class GetResultsInteractor(
    private val getUserId: () -> Flow<String>,
    private val getStars: (String) -> Flow<List<NameStars>>,
    private val getPartners: (String) -> Flow<List<String>>,
    private val getPartnersStars: (List<String>) -> Flow<Pair<String, List<NameStars>>>
) : BaseInteractor<List<Result>, Void?>() {

    override fun buildFlow(params: Void?): Flow<List<Result>> {
        return getUserId().flatMapLatest { userId ->
            getStars(userId).flatMapLatest { stars ->
                getPartners(userId).flatMapLatest { partners ->
                    flow {
                        emit(
                            getPartnersStars(partners).onStart { emit(Pair(userId, stars)) }
                                .toList()
                                .associateBy({ it.first }, { it.second })
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
        }
    }
}