package ru.drrey.babyname.names.api

import kotlinx.coroutines.flow.Flow
import ru.drrey.babyname.common.domain.entity.NameStars
import ru.drrey.babyname.common.domain.interactor.base.Interactor
import ru.drrey.babyname.navigationmediator.NamesFlowScreenProvider

interface NamesApi {
    fun getFlowScreenProvider(): NamesFlowScreenProvider
    fun countStarredNamesInteractor(): Interactor<Int, Void?>
    fun getStars(userId: String): Flow<List<NameStars>>
}