package com.template.mvvm

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.template.mvvm.models.*
import me.tatarka.bindingcollectionadapter2.ItemBinding

class ViewModelFactory private constructor(
        private val application: Application
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()
                    isAssignableFrom(MenViewModel::class.java) -> MenViewModel(
                            Injection.getInstance(application).provideRepository(application),
                            Injection.getInstance(application).itemOf(ProductItemViewModel::class.java) as ItemBinding<ProductItemViewModel>
                    )
                    isAssignableFrom(WomenViewModel::class.java) -> WomenViewModel(
                            Injection.getInstance(application).provideRepository(application),
                            Injection.getInstance(application).itemOf(ProductItemViewModel::class.java) as ItemBinding<ProductItemViewModel>
                    )
                    isAssignableFrom(AllGendersViewModel::class.java) -> AllGendersViewModel(
                            Injection.getInstance(application).provideRepository(application),
                            Injection.getInstance(application).itemOf(ProductItemViewModel::class.java) as ItemBinding<ProductItemViewModel>
                    )
                    isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel()
                    isAssignableFrom(ProductsViewModel::class.java) ->
                        ProductsViewModel(
                                Injection.getInstance(application).provideRepository(application),
                                Injection.getInstance(application).itemOf(ProductItemViewModel::class.java) as ItemBinding<ProductItemViewModel>
                        )
                    isAssignableFrom(AboutViewModel::class.java) -> AboutViewModel()
                    isAssignableFrom(SoftwareLicensesViewModel::class.java) ->
                        SoftwareLicensesViewModel(
                                application,
                                Injection.getInstance(application).provideRepository(application),
                                Injection.getInstance(application).itemOf(SoftwareLicenseItemViewModel::class.java) as ItemBinding<SoftwareLicenseItemViewModel>
                        )
                    isAssignableFrom(LicenseDetailViewModel::class.java) -> LicenseDetailViewModel()
                    isAssignableFrom(SoftwareLicenseItemViewModel::class.java) -> SoftwareLicenseItemViewModel()
                    isAssignableFrom(ProductItemViewModel::class.java) -> ProductItemViewModel()
                    isAssignableFrom(LicenseDetailViewModel::class.java) -> LicenseDetailViewModel()
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
