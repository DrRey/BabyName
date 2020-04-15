package ru.drrey.babyname.results.api

import ru.drrey.babyname.navigationmediator.ResultsFlowScreenProvider

interface ResultsApi {
    fun getResultsFlowScreenProvider(): ResultsFlowScreenProvider
}