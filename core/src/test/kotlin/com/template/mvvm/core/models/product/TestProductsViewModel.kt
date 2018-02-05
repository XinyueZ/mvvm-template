package com.template.mvvm.core.models.product

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.template.mvvm.repository.contract.ProductsDataSource
import io.kotlintest.properties.Gen
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.hamcrest.CoreMatchers.`is` as matchIs
import org.mockito.Mockito.`when` as mockWhen

class TestProductsViewModel {
    @Mock
    private lateinit var dataSource: ProductsDataSource
    @Mock
    private lateinit var lifecycle: Lifecycle

    private lateinit var vm: ProductsViewModel

    @Before
    fun setUp() {
        //MockitoAnnotations.initMocks(this)

        dataSource = mock(ProductsDataSource::class.java)

        lifecycle = mock(Lifecycle::class.java)
        mockWhen(lifecycle.currentState).thenReturn(Lifecycle.State.DESTROYED)

        vm = ProductsViewModel(dataSource)
    }

    @Test
    fun testRegisterLifecycleOwner() {
        val lifeThing = mock(LifecycleOwner::class.java)
        mockWhen(lifeThing.lifecycle).thenReturn(lifecycle)

        vm.registerLifecycleOwner(lifeThing)
        assertThat(
            lifeThing,
            `equalTo`(vm.lifecycleOwner)
        )

        val lifeThingOther = mock(LifecycleOwner::class.java)
        mockWhen(lifeThingOther.lifecycle).thenReturn(lifecycle)
        vm.registerLifecycleOwner(lifeThingOther)
        assertThat(
            lifeThingOther,
            `equalTo`(vm.lifecycleOwner)
        )
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun testOnBoundIndexOutOfBoundsException() {
        vm.onBound(Gen.negativeIntegers().generate())
    }
}