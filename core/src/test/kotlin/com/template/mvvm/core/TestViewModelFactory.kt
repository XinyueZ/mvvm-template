package com.template.mvvm.core

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.template.mvvm.core.models.about.AboutViewModel
import com.template.mvvm.core.models.home.HomeViewModel
import com.template.mvvm.core.models.license.LicenseDetailViewModel
import com.template.mvvm.core.models.license.SoftwareLicenseItemViewModel
import com.template.mvvm.core.models.license.SoftwareLicensesViewModel
import com.template.mvvm.core.models.product.AllGendersViewModel
import com.template.mvvm.core.models.product.MenViewModel
import com.template.mvvm.core.models.product.detail.ProductDetailViewModel
import com.template.mvvm.core.models.product.ProductItemViewModel
import com.template.mvvm.core.models.product.ProductsViewModel
import com.template.mvvm.core.models.product.WomenViewModel
import com.template.mvvm.core.models.splash.SplashViewModel
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.util.ReflectionHelpers

@RunWith(RobolectricTestRunner::class)
class TestViewModelFactory {
    @Test
    fun testSingleton() {
        val obj_1 = ViewModelFactory.getInstance(context().applicationContext as Application)
        val obj_2 = ViewModelFactory.getInstance(context().applicationContext as Application)
        assertThat(obj_1 === obj_2, `is`(true))
    }

    @Test
    fun testDestroyInstance() {
        val obj_1 = ViewModelFactory.getInstance(context().applicationContext as Application)
        ViewModelFactory.destroyInstance()
        val obj_2 = ReflectionHelpers.getStaticField<ViewModelFactory>(ViewModelFactory::class.java, "INSTANCE")
        assertThat(obj_1 === obj_2, `is`(false))
        assertThat(obj_2, `is`(nullValue()))

        val obj_3 = ViewModelFactory.getInstance(context().applicationContext as Application)
        assertThat(obj_3, `is`(notNullValue()))
        val obj_4 = ViewModelFactory.getInstance(context().applicationContext as Application)
        assertThat(obj_3 === obj_4, `is`(true))
    }

    @Test
    fun testCreate() {
        with(ViewModelFactory.getInstance(context().applicationContext as Application)) {
            assertThat(create(SplashViewModel::class.java), `is`(notNullValue()))
            assertThat(create(MenViewModel::class.java), `is`(notNullValue()))
            assertThat(create(WomenViewModel::class.java), `is`(notNullValue()))
            assertThat(create(AllGendersViewModel::class.java), `is`(notNullValue()))
            assertThat(create(HomeViewModel::class.java), `is`(notNullValue()))
            assertThat(create(ProductsViewModel::class.java), `is`(notNullValue()))
            assertThat(create(AboutViewModel::class.java), `is`(notNullValue()))
            assertThat(create(SoftwareLicensesViewModel::class.java), `is`(notNullValue()))
            assertThat(create(LicenseDetailViewModel::class.java), `is`(notNullValue()))
            assertThat(create(SoftwareLicenseItemViewModel::class.java), `is`(notNullValue()))
            assertThat(create(ProductItemViewModel::class.java), `is`(notNullValue()))
            assertThat(create(LicenseDetailViewModel::class.java), `is`(notNullValue()))
            assertThat(create(ProductDetailViewModel::class.java), `is`(notNullValue()))

            try {
                create(AndroidViewModel::class.java)
                assertThat(true, `is`(false))
            } catch (ex: IllegalArgumentException) {
                assertThat(ex, `is`(notNullValue()))
            }
        }
    }
}