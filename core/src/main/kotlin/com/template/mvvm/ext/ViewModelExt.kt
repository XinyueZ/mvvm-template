package com.template.mvvm.ext

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.template.mvvm.ViewModelFactory
import com.template.mvvm.models.Error
import com.template.mvvm.models.ErrorViewModel
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.channels.consumeEach

fun <T : ViewModel> FragmentActivity.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProviders.of(this, ViewModelFactory.getInstance(this.application)).get(viewModelClass)

fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>) =
        activity?.let { ViewModelProviders.of(it, ViewModelFactory.getInstance(it.application)).get(viewModelClass) }
                ?: kotlin.run { ViewModelProviders.of(this).get(viewModelClass) }

fun <T : ViewModel> LifecycleOwner.obtainViewModel(viewModelClass: Class<T>) = with(when (this) {
    is Fragment -> activity
    else -> this as FragmentActivity
}) {
    this?.let {
        ViewModelProviders.of(it, ViewModelFactory.getInstance(it.application)).get(viewModelClass)
    } ?: kotlin.run { throw IllegalStateException("LifecycleOwner is not a type of fragment or activity.") }
}

fun View.showErrorSnackbar(errorVm: Error, timeLength: Int = Snackbar.LENGTH_INDEFINITE) {
    Snackbar.make(this, errorVm.wording, timeLength).setAction(errorVm.retryWording, { errorVm.retry() }).show()
}

fun View.setupErrorSnackbar(lifecycleOwner: LifecycleOwner,
                            liveData: ErrorViewModel, timeLength: Int = Snackbar.LENGTH_INDEFINITE) {
    liveData.observe(lifecycleOwner, Observer {
        it?.let { showErrorSnackbar(it, timeLength) }
    })
}

fun View.onClick(block: suspend () -> Unit) {
    val eventActor = actor<Unit>(UI, capacity = Channel.CONFLATED) {
        // Handling only most recently received update.
        consumeEach { block() }
    }
    setOnClickListener {
        eventActor.offer(Unit)
    }
}

fun NavigationView.onNavigationItemSelected(block: suspend (Int) -> Unit) {
    val eventActor = actor<Int>(UI, capacity = Channel.CONFLATED) {
        // Handling only most recently received update.
        consumeEach { block(it) }
    }
    setNavigationItemSelectedListener {
        it.isChecked = true
        eventActor.offer(it.itemId)
        true
    }
}

fun BottomNavigationView.onNavigationItemSelected(block: suspend (Int) -> Unit) {
    val eventActor = actor<Int>(UI, capacity = Channel.CONFLATED) {
        // Handling only most recently received update.
        consumeEach { block(it) }
    }
    setOnNavigationItemSelectedListener {
        eventActor.offer(it.itemId)
        true
    }
}

fun Toolbar.onNavigationOnClick(block: suspend () -> Unit) {
    val eventActor = actor<Unit>(UI, capacity = Channel.CONFLATED) {
        // Handling only most recently received update.
        consumeEach { block() }
    }
    setNavigationOnClickListener {
        eventActor.offer(Unit)
    }
}




