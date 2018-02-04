package com.template.mvvm.core.models.product

import android.arch.lifecycle.LifecycleOwner
import io.kotlintest.properties.Gen
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.hamcrest.CoreMatchers.`is` as matchIs
import org.mockito.Mockito.`when` as mockWhen

class TestProductsViewModel {
    @Mock
    private lateinit var vm: ProductsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testRegisterLifecycleOwner() {
        val lifeThing = mock(LifecycleOwner::class.java)
        mockWhen(vm.lifecycleOwner).thenReturn(lifeThing)
        assertThat(
            vm.lifecycleOwner, `is`(notNullValue())
        )
        assertThat(
            lifeThing,
            `equalTo`(vm.lifecycleOwner)
        )

        val lifeThingOther = mock(LifecycleOwner::class.java)
        vm.registerLifecycleOwner(lifeThingOther)
        mockWhen(vm.lifecycleOwner).thenReturn(lifeThingOther)
        assertThat(
            vm.lifecycleOwner, `is`(notNullValue())
        )
        assertThat(
            lifeThingOther,
            `equalTo`(vm.lifecycleOwner)
        )
    }

    @Test
    fun testOnBound() {
        mockWhen(vm.onBound(Gen.negativeIntegers().generate())).thenThrow(IndexOutOfBoundsException())
    }
}