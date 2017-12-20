package com.template.mvvm

import android.app.Application
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
}