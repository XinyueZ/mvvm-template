package com.template.mvvm.core

import com.template.mvvm.repository.RepositoryInjection
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.nullValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.util.ReflectionHelpers

@RunWith(RobolectricTestRunner::class)
class TestCoreModule {
    private lateinit var coreModule: CoreModule
    @Rule
    fun test() = CoreTestRule()

    @Before
    fun setup() {
        coreModule = CoreModule(context())
    }

    @Test
    fun testOnCoreStop() {
        coreModule.onCoreStop()
        assertThat(ReflectionHelpers.getStaticField<ViewModelFactory>(ViewModelFactory::class.java, "INSTANCE"), `is`(nullValue()))
        assertThat(ReflectionHelpers.getStaticField<RepositoryInjection>(RepositoryInjection::class.java, "INSTANCE"), `is`(nullValue()))
//        assertThat(ReflectionHelpers.getStaticField<Repository?>(RepositoryInjection::class.java, "DS_INSTANCE"), `is`(nullValue()))
    }
}