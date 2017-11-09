package com.template.mvvm

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher

class IoToMainScheduleObservable<T> : ObservableTransformer<T, T> {
    override fun apply(upstream: Observable<T>): Observable<T> {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

class IoToIoScheduleObservable<T> : ObservableTransformer<T, T> {
    override fun apply(upstream: Observable<T>): Observable<T> {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
    }
}

class IoToMainScheduleFlowable<T> : FlowableTransformer<T, T> {
    override fun apply(upstream: Flowable<T>): Publisher<T> {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

class IoToMainScheduleSingle<T> : SingleTransformer<T, T> {
    override fun apply(upstream: Single<T>): Single<T> {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

class IoToMainScheduleMaybe<T> : MaybeTransformer<T, T> {
    override fun apply(upstream: Maybe<T>): Maybe<T> {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

class IoToMainScheduleCompletable : CompletableTransformer {
    override fun apply(upstream: Completable): CompletableSource {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

class IoToIoScheduleSingle<T> : SingleTransformer<T, T> {
    override fun apply(upstream: Single<T>): Single<T> {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
    }
}

class IoToIoScheduleCompletable : CompletableTransformer {
    override fun apply(upstream: Completable): Completable {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
    }
}


class IoToIoScheduleFlowable<T> : FlowableTransformer<T, T> {
    override fun apply(upstream: Flowable<T>): Flowable<T> {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
    }
}

class IoToComputationScheduleSingle<T> : SingleTransformer<T, T> {
    override fun apply(upstream: Single<T>): Single<T> {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
    }
}



class IoToComputationScheduleFlowable<T> : FlowableTransformer<T, T> {
    override fun apply(upstream: Flowable<T>): Flowable<T> {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
    }
}

class ComputationToMainScheduleSingle<T> : SingleTransformer<T, T> {
    override fun apply(upstream: Single<T>): Single<T> {
        return upstream.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
    }
}