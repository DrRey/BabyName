package ru.drrey.babyname.common.domain.executor

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers


/**
 * UI thread. A PostExecutionThread that runs on the main thread.
 */
class UIThread : PostExecutionThread {
    override val scheduler: Scheduler
        get() = AndroidSchedulers.mainThread()
}