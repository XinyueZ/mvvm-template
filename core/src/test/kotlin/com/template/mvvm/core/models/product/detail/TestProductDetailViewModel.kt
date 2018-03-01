package com.template.mvvm.core.models.product.detail

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.databinding.Observable
import com.template.mvvm.base.ext.lang.toHtml
import com.template.mvvm.core.generateProductDetail
import com.template.mvvm.core.models.registerLifecycleOwner
import com.template.mvvm.core.sleepWhile
import com.template.mvvm.repository.contract.ProductsDataSource
import io.kotlintest.properties.Gen
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.hamcrest.CoreMatchers.`is` as matchIs
import org.mockito.Mockito.`when` as mockWhen

@RunWith(RobolectricTestRunner::class)
class TestProductDetailViewModel {
    @Mock
    private lateinit var application: Application
    @Mock
    private lateinit var dataSource: ProductsDataSource
    @Mock
    private lateinit var lifeOwner: LifecycleOwner
    private lateinit var lifecycle: LifecycleRegistry

    private lateinit var vm: ProductDetailViewModel

    @Before
    fun setUp() {
        //MockitoAnnotations.initMocks(this)
        application = Mockito.mock(Application::class.java)
        dataSource = Mockito.mock(ProductsDataSource::class.java)
        lifeOwner = Mockito.mock(LifecycleOwner::class.java)
        lifecycle = LifecycleRegistry(lifeOwner)
        Mockito.`when`(lifeOwner.lifecycle).thenReturn(lifecycle)
        vm = ProductDetailViewModel(application, dataSource)
    }

    @Test
    fun testShowingDetail() = runBlocking {
        val pid = Gen.positiveIntegers().generate().toLong()
        val detail = generateProductDetail(pid).generate()

        vm.productIdToDetail = pid

        mockWhen(
            dataSource.getProductDetail(CommonPool, pid)
        ).thenReturn(produce(CommonPool) { send(detail) })

        vm.state.productId.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                vm.onBound()
            }
        })
        vm.registerLifecycleOwner(lifeOwner)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)

        sleepWhile {
            vm.state.productTitle.get() == null
        }



        assertThat(
            vm.productIdToDetail,
            equalTo(pid)
        )

        assertThat(
            vm.state.productTitle.get(),
            equalTo(detail.title)
        )

        assertThat(
            vm.state.productDescription.get(),
            equalTo(detail.description.toHtml())
        )

        assertThat(
            vm.state.productImageUris.size,
            equalTo(detail.pictures.size)
        )

        var contained = false
        vm.state.productImageUris.forEach { uriSearch ->
            detail.pictures.map { it.uri }.forEach { uriExpected ->
                if (!contained && uriExpected.toString().contentEquals(uriSearch.toString())) {
                    contained = true
                }
            }
            assertThat(
                true,
                equalTo(contained)
            )
        }
    }
}