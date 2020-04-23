package ru.drrey.babyname.common.domain.interactor.base

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch


/**
 * Base interactor class. Thread executor and post execution thread are injected.
 * Provides observable execution and dispose methods.
 */
abstract class BaseInteractor<T, in Params> {
    private var currentJob: Job? = null

    abstract fun buildFlow(params: Params): Flow<T>

    /**
     * Executes the current use case.
     * @param collector
     * *
     * @param params Parameters (Optional) used to build/execute this use case.
     */
    fun execute(
        scope: CoroutineScope,
        params: Params,
        onError: ((Exception) -> Unit)? = null,
        onCompletion: (() -> Unit)? = null,
        collector: (T) -> Unit
    ) {
        currentJob?.cancel()
        val flow = buildFlow(params).flowOn(Dispatchers.Default).conflate()
        currentJob = scope.launch {
            try {
                flow.collect { collector.invoke(it) }
            } catch (e: Exception) {
                Log.e("Interactor onError", e.toString())
                onError?.invoke(e)
            }
            onCompletion?.invoke()
        }
    }

    /**
     * Cancel current [Job].
     */
    fun dispose() {
        if (currentJob?.isActive == true) {
            currentJob?.cancel()
        }
    }
}