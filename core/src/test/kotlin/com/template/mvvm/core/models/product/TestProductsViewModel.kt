package com.template.mvvm.core.models.product

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.Observer
import com.template.mvvm.core.models.registerLifecycleOwner
import com.template.mvvm.core.sleepWhile
import com.template.mvvm.repository.contract.ProductsDataSource
import com.template.mvvm.repository.domain.products.Product
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
import org.robolectric.util.ReflectionHelpers
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
        vm = ProductsViewModel(dataSource)
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun testOnBoundIndexOutOfBoundsException() {
        vm.onBound(Gen.negativeIntegers().generate())
    }

    @Test
    fun testOffsetAfterPagings() = runBlocking {
        val size = 10
        val pages = Gen.choose(0, 200).generate()
        println("size: $size, pages: $pages")
        (0 until pages)
            .asSequence()
            .map { it * size }
            .forEach { offset ->
                mockWhen(
                    dataSource.getAllProducts(
                        CommonPool,
                        offset,
                        true
                    )
                ).thenReturn(produce(CommonPool) { send(generateProductList(size).generate()) })
            }

        vm.registerLifecycleOwner(lifeOwner)
        vm.collectionItemVmList.observe(lifeOwner, Observer {
            vm.onBound(if (vm.getCurrentOffset() == 0) 0 else vm.getCurrentOffset() - 1)
        })
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)
        sleepWhile {
            println("current: ${vm.getCurrentOffset()}")
            vm.getCurrentOffset() != size * pages
        }
        assertThat(
            vm.getCurrentOffset(),
            `equalTo`(size * pages)
        )
    }

    @Test
    fun testOffsetAfterReload() {
//        runBlocking {
        //            val lifeThing = mock(LifecycleOwner::class.java)
//            mockWhen(lifeThing.lifecycle).thenReturn(lifecycle)

//            launch(UI) {
//                val size = 10
//                val pages = Gen.choose(1, 200).generate()
//
//                (0 until pages)
//                    .asSequence()
//                    .map { it * size }
//                    .forEach { offset ->
//                        mockWhen(
//                            dataSource.getAllProducts(
//                                coroutineContext,
//                                offset,
//                                true
//                            )
//                        ).thenReturn(
//                            produce(coroutineContext) {
//                                send(generateProductList(size).generate())
//                            })
//                    }
//                mockWhen(dataSource.deleteAll(coroutineContext)).thenReturn(
//                    produce(coroutineContext) {
//                        send(Unit)
//                    })
//
//                vm.reloadData()
//
//                assertThat(
//                    vm.getCurrentOffset(),
//                    `greaterThan`(0)
//                )
//                assertThat(
//                    vm.getCurrentOffset(),
//                    `equalTo`(size)
//                )
//            }
//        }
    }

    private fun ProductsViewModel.getCurrentOffset() =
        ReflectionHelpers.getField<Int>(this, "offset")

    private fun generateProductList(size: Int) = object : Gen<List<Product>> {
        override fun generate(): List<Product> = mutableListOf<Product>().apply {
            for (i in 0 until size) {
                add(Product(Gen.positiveIntegers().generate().toLong()))
            }
        }
    }
}