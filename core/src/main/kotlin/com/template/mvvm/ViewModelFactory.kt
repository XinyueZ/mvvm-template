package com.template.mvvm

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.template.mvvm.models.*

class ViewModelFactory private constructor(
        private val application: Application
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()
                    isAssignableFrom(MenViewModel::class.java) -> MenViewModel(
                            RepositoryInjection.getInstance(application).provideRepository(application))
                    isAssignableFrom(WomenViewModel::class.java) -> WomenViewModel(
                            RepositoryInjection.getInstance(application).provideRepository(application))
                    isAssignableFrom(AllGendersViewModel::class.java) -> AllGendersViewModel(
                            RepositoryInjection.getInstance(application).provideRepository(application))
                    isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel()
                    isAssignableFrom(ProductsViewModel::class.java) ->
                        ProductsViewModel(RepositoryInjection.getInstance(application).provideRepository(application))
                    isAssignableFrom(AboutViewModel::class.java) -> AboutViewModel()
                    isAssignableFrom(SoftwareLicensesViewModel::class.java) ->
                        SoftwareLicensesViewModel(
                                application,
                                RepositoryInjection.getInstance(application).provideRepository(application))
                    isAssignableFrom(LicenseDetailViewModel::class.java) -> LicenseDetailViewModel()
                    isAssignableFrom(SoftwareLicenseItemViewModel::class.java) -> SoftwareLicenseItemViewModel()
                    isAssignableFrom(ProductItemViewModel::class.java) -> ProductItemViewModel()
                    isAssignableFrom(LicenseDetailViewModel::class.java) -> LicenseDetailViewModel()
                    isAssignableFrom(ProductDetailViewModel::class.java) -> ProductDetailViewModel(
                            RepositoryInjection.getInstance(application).provideRepository(application)
                    )
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
