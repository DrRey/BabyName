package ru.drrey.babyname.results.domain.interactor

import kotlinx.coroutines.flow.*
import ru.drrey.babyname.common.domain.entity.NameStars
import ru.drrey.babyname.common.domain.interactor.base.Interactor
import ru.drrey.babyname.results.domain.entity.Result

/**
 * Get results interactor. Results are calculated as follows:
 * 1) If all partners banned the name - the name is not shown
 * 2) If some partners banned the name - the ban is calculated as 0 stars
 * 3) If the name is not banned but is not yet rated - the result is unaffected
 */
class GetResultsInteractor(
    private val getUserId: () -> Flow<String>,
    private val getStars: (String) -> Flow<List<NameStars>>,
    private val getPartners: (String) -> Flow<List<String>>,
    private val getPartnersStars: (List<String>) -> Flow<Pair<String, List<NameStars>>>
) : Interactor<List<Result>, Nothing?>() {

    override fun buildFlow(params: Nothing?): Flow<List<Result>> {
        return getUserId().flatMapLatest { userId ->
            getStars(userId).flatMapLatest { stars ->
                getPartners(userId).flatMapLatest { partners ->
                    flow {
                        emit(
                            getPartnersStars(partners).onStart { emit(Pair(userId, stars)) }
                                .toList()
                                .associateBy({ it.first }, { it.second })
                                .values.asSequence() //getting just the values sequence
                                .flatten() //getting the whole list of stars
                                .groupBy(  //grouping by name and stars
                                    { nameStars -> nameStars.name },
                                    { nameStars ->
                                        nameStars.stars ?: -1
                                    })
                                .filterNot { //removing names filtered by all partners
                                    it.value.all { stars -> stars == -1 }
                                }
                                .mapValues { //removing unset stars
                                    it.value.filter { stars -> stars != 0 }
                                }
                                .mapValues { //setting filtered names as 0 stars
                                    it.value.map { stars -> if (stars < 0) 0 else stars }
                                }
                                .mapValues { it.value.average() } //mapping values to average
                                .map { //mapping to results list
                                    Result(
                                        it.key,
                                        it.value.toFloat()
                                    )
                                }
                                .sortedByDescending { it.averageStars } //sorting
                        )
                    }
                }
            }
        }
    }
}