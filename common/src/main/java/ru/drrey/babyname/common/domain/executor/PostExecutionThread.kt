package ru.drrey.babyname.common.domain.executor

import io.reactivex.Scheduler


/**
 * Post execution thread interface
 */
interface PostExecutionThread {
    val scheduler: Scheduler
}