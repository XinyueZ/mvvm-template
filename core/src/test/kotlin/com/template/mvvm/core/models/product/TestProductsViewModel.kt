package com.template.mvvm.core.models.product

import android.arch.lifecycle.LifecycleOwner
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import org.hamcrest.CoreMatchers.`is` as matchIs
import org.mockito.Mockito.`when` as mockWhen

class TestProductsViewModel {
    private lateinit var vm: ProductsViewModel

    @Rule
    fun test() = MockitoJUnit.rule()

    @Before
    fun setUp() {
        vm = mock(ProductsViewModel::class.java)
    }

    @Test
    fun testRegisterLifecycleOwner() {
        // Define logical
        val lifeThing = mock(LifecycleOwner::class.java)
        mockWhen(vm.lifecycleOwner).thenReturn(lifeThing)

        // Complete logical
        vm.registerLifecycleOwner(lifeThing)

        // Verify result of logical
        verify(vm).registerLifecycleOwner(lifeThing)
        assertThat(
            vm.lifecycleOwner, `is`(notNullValue())
        )
        assertThat(
            lifeThing,
            `equalTo`(vm.lifecycleOwner)
        )
    }
}