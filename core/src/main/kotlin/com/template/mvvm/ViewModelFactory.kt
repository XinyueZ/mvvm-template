package com.template.mvvm

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.template.mvvm.models.AboutViewModel
import com.template.mvvm.models.AllGendersViewModel
import com.template.mvvm.models.HomeViewModel
import com.template.mvvm.models.LicenseDetailViewModel
import com.template.mvvm.models.MenViewModel
import com.template.mvvm.models.ProductDetailViewModel
import com.template.mvvm.models.ProductItemViewModel
import com.template.mvvm.models.ProductsViewModel
import com.template.mvvm.models.SoftwareLicenseItemViewModel
import com.template.mvvm.models.SoftwareLicensesViewModel
import com.template.mvvm.models.SplashViewModel
import com.template.mvvm.models.WomenViewModel

class ViewModelFactory private constructor(
        private val application: Application
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()
                    isAssignableFrom(MenViewModel::class.java) -> MenViewModel(
                            RepositoryInjection.getInstance().provideRepository(application))
                    isAssignableFrom(WomenViewModel::class.java) -> WomenViewModel(
                            RepositoryInjection.getInstance().provideRepository(application))
                    isAssignableFrom(AllGendersViewModel::class.java) -> AllGendersViewModel(
                            RepositoryInjection.getInstance().provideRepository(application))
                    isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel()
                    isAssignableFrom(ProductsViewModel::class.java) ->
                        ProductsViewModel(RepositoryInjection.getInstance().provideRepository(application))
                    isAssignableFrom(AboutViewModel::class.java) -> AboutViewModel()
                    isAssignableFrom(SoftwareLicensesViewModel::class.java) ->
                        SoftwareLicensesViewModel(
                                application,
                                RepositoryInjection.getInstance().provideRepository(application))
                    isAssignableFrom(LicenseDetailViewModel::class.java) -> LicenseDetailViewModel()
                    isAssignableFrom(SoftwareLicenseItemViewModel::class.java) -> SoftwareLicenseItemViewModel()
                    isAssignableFrom(ProductItemViewModel::class.java) -> ProductItemViewModel()
                    isAssignableFrom(LicenseDetailViewModel::class.java) -> LicenseDetailViewModel()
                    isAssignableFrom(ProductDetailViewModel::class.java) -> ProductDetailViewModel(
                            RepositoryInjection.getInstance().provideRepository(application)
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
