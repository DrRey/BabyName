package ru.drrey.babyname.common.presentation.base

import io.reactivex.observers.DisposableObserver

class InteractorObserver<T> : DisposableObserver<T>() {
    private var onCompleteListener: (() -> Unit)? = null
    private var onNextListener: ((T) -> Unit)? = null
    private var onErrorListener: ((Throwable) -> Unit)? = null

    fun onComplete(f: () -> Unit): InteractorObserver<T> {
        onCompleteListener = f
        return this
    }

    fun onNext(f: (T) -> Unit): InteractorObserver<T> {
        onNextListener = f
        return this
    }

    fun onError(f: (Throwable) -> Unit): InteractorObserver<T> {
        onErrorListener = f
        return this
    }

    override fun onComplete() {
        onCompleteListener?.invoke()
    }

    override fun onNext(t: T) {
        onNextListener?.invoke(t)
    }

    override fun onError(e: Throwable) {
        onErrorListener?.invoke(e)
    }
}