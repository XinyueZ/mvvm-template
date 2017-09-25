package com.template.mvvm

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.template.mvvm.models.*
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 * A creator is used to inject the product ID into the ViewModel
 *
 *
 * This creator is to showcase how to inject dependencies into ViewModels. It's not
 * actually necessary in this case, as the product ID can be passed in a public method.
 */
class ViewModelFactory private constructor(
        private val application: Application
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()
                    isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel()
                    isAssignableFrom(AppNavigationViewModel::class.java) -> AppNavigationViewModel()
                    isAssignableFrom(ProductsViewModel::class.java) ->
                        ProductsViewModel(Injection.getInstance(application).provideRepository(application),
                                Injection.getInstance(application).itemOf(ProductItemViewModel::class.java) as ItemBinding<ProductItemViewModel>)
                    isAssignableFrom(AboutViewModel::class.java) -> AboutViewModel()
                    isAssignableFrom(SoftwareLicensesViewModel::class.java) ->
                        SoftwareLicensesViewModel(Injection.getInstance(application).provideRepository(application),
                                Injection.getInstance(application).itemOf(SoftwareLicenseItemViewModel::class.java) as ItemBinding<SoftwareLicenseItemViewModel>)
                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile private var INSTANCE: ViewModelFactory? = null

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
