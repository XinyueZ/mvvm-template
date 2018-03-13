package com.template.mvvm.core

import android.app.Application
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.template.mvvm.core.models.about.AboutViewModel
import com.template.mvvm.core.models.home.HomeViewModel
import com.template.mvvm.core.models.license.LicenseDetailViewModel
import com.template.mvvm.core.models.license.SoftwareLicenseItemViewModel
import com.template.mvvm.core.models.license.SoftwareLicensesViewModel
import com.template.mvvm.core.models.product.AllGendersViewModel
import com.template.mvvm.core.models.product.MenViewModel
import com.template.mvvm.core.models.product.ProductItemViewModel
import com.template.mvvm.core.models.product.WomenViewModel
import com.template.mvvm.core.models.product.category.CategoriesProductsViewModel
import com.template.mvvm.core.models.product.detail.ProductDetailViewModel
import com.template.mvvm.core.models.splash.SplashViewModel
import com.template.mvvm.repository.RepositoryInjection
import kotlin.reflect.KClass

class ViewModelFactory private constructor(
    private val application: Application
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()
                isAssignableFrom(MenViewModel::class.java) -> MenViewModel(
                    RepositoryInjection.getInstance().provideRepository(application)
                )
                isAssignableFrom(WomenViewModel::class.java) -> WomenViewModel(
                    RepositoryInjection.getInstance().provideRepository(application)
                )
                isAssignableFrom(AllGendersViewModel::class.java) -> AllGendersViewModel(
                    RepositoryInjection.getInstance().provideRepository(application)
                )
                isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel()
                isAssignableFrom(AboutViewModel::class.java) -> AboutViewModel()
                isAssignableFrom(SoftwareLicensesViewModel::class.java) ->
                    SoftwareLicensesViewModel(
                        application,
                        RepositoryInjection.getInstance().provideRepository(application)
                    )
                isAssignableFrom(LicenseDetailViewModel::class.java) -> LicenseDetailViewModel()
                isAssignableFrom(SoftwareLicenseItemViewModel::class.java) -> SoftwareLicenseItemViewModel()
                isAssignableFrom(ProductItemViewModel::class.java) -> ProductItemViewModel()
                isAssignableFrom(LicenseDetailViewModel::class.java) -> LicenseDetailViewModel()
                isAssignableFrom(ProductDetailViewModel::class.java) -> ProductDetailViewModel(
                    application,
                    RepositoryInjection.getInstance().provideRepository(application)
                )
                isAssignableFrom(CategoriesProductsViewModel::class.java) -> CategoriesProductsViewModel()
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

    companion object {

        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(application)
                    .also { INSTANCE = it }
            }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}

fun <T : ViewModel> FragmentActivity.obtainViewModel(viewModelClass: Class<T>) =
    ViewModelProviders.of(
        this,
        ViewModelFactory.getInstance(this.application)
    ).get(viewModelClass)

fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>) =
    activity?.let {
        ViewModelProviders.of(
            it,
            ViewModelFactory.getInstance(it.application)
        ).get(viewModelClass)
    } ?: kotlin.run { ViewModelProviders.of(this).get(viewModelClass) }

fun <T : ViewModel> LifecycleOwner.obtainViewModel(viewModelClass: Class<T>) = with(
    when (this) {
        is Fragment -> activity
        else -> this as FragmentActivity
    }
) {
    this?.let {
        ViewModelProviders.of(
            it,
            ViewModelFactory.getInstance(it.application)
        ).get(viewModelClass)
    }
            ?: kotlin.run { throw IllegalStateException("LifecycleOwner is not a type of fragment or activity.") }
}



fun <VM : ViewModel, VMC : KClass<VM>> VMC.get(
    activity: FragmentActivity?,
    block: VM.() -> Unit
) {
    activity?.run {
        obtainViewModel(java).apply {
            block()
        }
    }
}


fun <VM : ViewModel, VMC : KClass<VM>> VMC?.get(
    lifecycleOwner: LifecycleOwner?,
    block: VM.() -> Unit
) {
    this?.run {
        lifecycleOwner?.run {
            lifecycleOwner.obtainViewModel(java).apply {
                block()
            }
        }
    }
}

fun <VM : ViewModel, VMC : Class<VM>> VMC?.get(
    lifecycleOwner: LifecycleOwner?,
    block: VM.() -> Unit
) {
    this?.run {
        lifecycleOwner?.run {
            lifecycleOwner.obtainViewModel(this@get).apply {
                block()
            }
        }
    }
}