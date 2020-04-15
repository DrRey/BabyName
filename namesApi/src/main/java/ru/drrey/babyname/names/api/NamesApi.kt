package ru.drrey.babyname.names.api

import io.reactivex.Single
import ru.drrey.babyname.common.domain.entity.NameStars
import ru.drrey.babyname.common.domain.interactor.base.BaseInteractor
import ru.drrey.babyname.navigationmediator.NamesFlowScreenProvider

interface NamesApi {
    fun getFlowScreenProvider(): NamesFlowScreenProvider
    fun countStarredNamesInteractor(): BaseInteractor<Int, Void?>
    fun getStars(userId: String): Single<List<NameStars>>
}