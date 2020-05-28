package ru.drrey.babyname.names.api

import kotlinx.coroutines.flow.Flow
import ru.drrey.babyname.common.domain.entity.NameStars
import ru.drrey.babyname.common.domain.interactor.base.Interactor
import ru.drrey.babyname.navigationmediator.FilterFlowScreenProvider
import ru.drrey.babyname.navigationmediator.NamesFlowScreenProvider

interface NamesApi {
    fun getNamesFlowScreenProvider(): NamesFlowScreenProvider
    fun getFilterFlowScreenProvider(): FilterFlowScreenProvider
    fun countStarredNamesInteractor(): Interactor<Int, Nothing?>
    fun getStars(userId: String): Flow<List<NameStars>>
    fun getSexFilterInteractor(): Interactor<Sex?, Nothing?>
    fun setSexFilterInteractor(): Interactor<Unit, Sex?>
    fun countUnfilteredNamesInteractor(): Interactor<Int, Nothing?>
}