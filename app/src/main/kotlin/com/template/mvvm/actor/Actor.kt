package com.template.mvvm.actor

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v4.util.SimpleArrayMap
import android.util.Log
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import kotlin.reflect.KClass

abstract class Actor<T> {
    private val publisher = PublishSubject.create<Message<T>>()

    fun post(msg: Message<T>) {
        publisher.onNext(msg)
    }

    fun start(subject: LifecycleRegistryOwner) = Factory(subject, publisher)

    class LifecycleObserverForActor<T> internal constructor(
            private val publisher: PublishSubject<Message<T>>,
            private val successMap: SimpleArrayMap<KClass<*>, (Message<T>) -> Unit>,
            private val error: ((Throwable) -> Unit)?) : LifecycleObserver {

        private val TAG = "ForActor"
        private var disposable: Disposable? = null

        private fun onSuccess(msg: Message<T>) {
            successMap.get(msg::class)?.let { it(msg) }
        }

        private fun onError(throwable: Throwable) {
            error?.let { it(throwable) }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onResume() {
            Log.d(TAG, "onResume")
            disposable = publisher.subscribe({ this.onSuccess(it) }) { this.onError(it) }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun onPause() {
            Log.d(TAG, "onPause")
            disposable?.let {
                it.apply { dispose() }.also { disposable = null }
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            Log.d(TAG, "onDestroy")
            successMap.clear()
        }
    }

    class Factory<T> internal constructor(private val subject: LifecycleRegistryOwner, private val publisher: PublishSubject<Message<T>>) {
        private var error: ((Throwable) -> Unit)? = null
        private val success = SimpleArrayMap<KClass<*>, (Message<T>) -> Unit>()

        fun subscribe(clazz: KClass<*>, success: (Message<T>) -> Unit): Factory<T> {
            this.success.put(clazz, success)
            return this
        }

        fun subscribeError(error: ((Throwable) -> Unit)?): Factory<T> {
            if (this.error != null) {
                throw IllegalStateException("Error consumer already defined!")
            }
            this.error = error
            return this
        }

        fun register() {
            val observer = LifecycleObserverForActor(publisher, success, error)
            subject.lifecycle
                    .addObserver(observer)
        }
    }
}
