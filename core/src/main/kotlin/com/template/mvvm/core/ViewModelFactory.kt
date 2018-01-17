package com.template.mvvm.core

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.template.mvvm.core.models.about.AboutViewModel
import com.template.mvvm.core.models.home.HomeViewModel
import com.template.mvvm.core.models.license.LicenseDetailViewModel
import com.template.mvvm.core.models.license.SoftwareLicenseItemViewModel
import com.template.mvvm.core.models.license.SoftwareLicensesViewModel
import com.template.mvvm.core.models.product.AllGendersViewModel
import com.template.mvvm.core.models.product.MenViewModel
import com.template.mvvm.core.models.product.ProductDetailViewModel
import com.template.mvvm.core.models.product.ProductItemViewModel
import com.template.mvvm.core.models.product.ProductsViewModel
import com.template.mvvm.core.models.product.WomenViewModel
import com.template.mvvm.core.models.splash.SplashViewModel
import com.template.mvvm.repository.RepositoryInjection

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
                isAssignableFrom(ProductsViewModel::class.java) ->
                    ProductsViewModel(
                        RepositoryInjection.getInstance().provideRepository(
                            application
                        )
                    )
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
                    RepositoryInjection.getInstance().provideRepository(application)
                )
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
