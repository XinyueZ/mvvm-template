package com.template.mvvm.life

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.template.mvvm.home.HomeViewModel
import com.template.mvvm.products.ProductsViewModel
import com.template.mvvm.splash.SplashViewModel

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
                    isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel(application)
                    isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(application)
                    isAssignableFrom(ProductsViewModel::class.java) -> ProductsViewModel(application)
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
