package ru.drrey.babyname.common.domain.interactor.base

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import ru.drrey.babyname.common.domain.executor.PostExecutionThread
import ru.drrey.babyname.common.domain.executor.ThreadExecutor


/**
 * Base interactor class. Thread executor and post execution thread are injected.
 * Provides observable execution and dispose methods.
 */
abstract class BaseInteractor<T, in Params>(
    private val threadExecutor: ThreadExecutor,
    private val postExecutionThread: PostExecutionThread
) {
    private val disposables: CompositeDisposable = CompositeDisposable()


    abstract fun buildUseCaseObservable(params: Params): Observable<T>

    /**
     * Executes the current use case.
     * @param observer [DisposableObserver] which will be listening to the observable build
     * * by [.buildUseCaseObservable] ()} method.
     * *
     * @param params Parameters (Optional) used to build/execute this use case.
     */
    fun execute(params: Params, observer: DisposableObserver<T>) {
        val observable = this.buildUseCaseObservable(params)
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.scheduler)
        addDisposable(observable.subscribeWith(observer))
    }

    /**
     * Dispose from current [CompositeDisposable].
     */
    fun dispose() {
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }

    /**
     * Dispose from current [CompositeDisposable].
     */
    private fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }
}