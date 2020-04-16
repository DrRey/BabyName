package ru.drrey.babyname.results.domain.interactor

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ru.drrey.babyname.common.domain.entity.NameStars
import ru.drrey.babyname.common.domain.interactor.base.BaseInteractor
import ru.drrey.babyname.results.domain.entity.Result

/**
 * Get results interactor
 */
@ExperimentalCoroutinesApi
class GetResultsInteractor(
    private val getUserId: () -> Flow<String>,
    private val getStars: (String) -> Flow<List<NameStars>>,
    private val getPartners: (String) -> Flow<List<String>>,
    private val getPartnersStars: (List<String>) -> Flow<Map<String, List<NameStars>>>
) : BaseInteractor<List<Result>, Void?>() {

    override fun buildFlow(params: Void?): Flow<List<Result>> {
        return getUserId().flatMapLatest { userId ->
            getStars(userId).flatMapLatest { stars ->
                getPartners(userId).flatMapLatest { partners ->
                    getPartnersStars(partners).map { partnersStars ->
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
                    }
                }
            }
        }
    }
}