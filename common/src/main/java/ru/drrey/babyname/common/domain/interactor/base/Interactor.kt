package ru.drrey.babyname.common.domain.interactor.base

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn


/**
 * Base interactor class. Thread executor and post execution thread are injected.
 * Provides observable execution and dispose methods.
 */
abstract class Interactor<T, in Params> {
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
        onSuccess: (() -> Unit)? = null,
        finally: (() -> Unit)? = null,
        collector: ((T) -> Unit)? = null
    ) {
        cancel()
        val flow = buildFlow(params).flowOn(Dispatchers.Default).conflate()
        currentJob = scope.launch {
            try {
                flow.collect { collector?.invoke(it) }
                onSuccess?.invoke()
            } catch (e: Exception) {
                if (e !is CancellationException) {
                    Log.e("Interactor onError", e.toString())
                    onError?.invoke(e)
                }
            } finally {
                finally?.invoke()
            }
        }
    }

    /**
     * Cancel current [Job].
     */
    fun cancel() {
        if (currentJob?.isActive == true) {
            currentJob?.cancel()
        }
    }
}