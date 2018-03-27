package com.template.mvvm.core.models.product

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import com.template.mvvm.base.ext.android.arch.lifecycle.setupObserve
import com.template.mvvm.core.arch.registerLifecycleOwner
import com.template.mvvm.core.generateProductList
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
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import org.hamcrest.CoreMatchers.`is` as matchIs
import org.mockito.Mockito.`when` as mockWhen

@RunWith(RobolectricTestRunner::class)
class TestProductsViewModel {
    @Mock
    private lateinit var dataSource: ProductsDataSource
    @Mock
    private lateinit var lifeOwner: LifecycleOwner
    private lateinit var lifecycle: LifecycleRegistry

    private lateinit var vm: ProductsViewModel

    @Before
    fun setUp() {
        //MockitoAnnotations.initMocks(this)
        dataSource = mock(ProductsDataSource::class.java)
        lifeOwner = mock(LifecycleOwner::class.java)
        lifecycle = LifecycleRegistry(lifeOwner)
        mockWhen(lifeOwner.lifecycle).thenReturn(lifecycle)
        vm = AllGendersViewModel(dataSource)
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun testOnBoundIndexOutOfBoundsException() {
        vm.onBound(Gen.negativeIntegers().generate())
    }

    @Test
    fun testOffsetAfterPagings() = runBlocking {
        var offset = 0
        val size = 10
        val pages = Gen.choose(0, 200).generate()
        (0 until pages)
            .asSequence()
            .map { it * size }
            .forEach {
                mockWhen(
                    dataSource.getAllProducts(
                        CommonPool,
                        it,
                        true
                    )
                ).thenReturn(produce(CommonPool) {
                    offset += size
                    send(generateProductList(size).generate())
                })
            }

        vm.registerLifecycleOwner(lifeOwner)
        vm.controller.collectionItemVmList.setupObserve(lifeOwner) {
            vm.onBound(if (offset == 0) 0 else offset - 1)
        }
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)
        val predication = size * pages
        sleepWhile {
            offset != predication
        }
        assertThat(
            offset,
            `equalTo`(predication)
        )
    }

}