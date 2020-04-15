package ru.drrey.babyname.results.presentation

import ru.drrey.babyname.results.domain.entity.Result

sealed class ResultsState

object ResultsNotLoaded : ResultsState()
object ResultsLoading : ResultsState()
class ResultsLoaded(val results: List<Result>) : ResultsState()
class ResultsLoadError(val t: Throwable, val message: String?) : ResultsState()