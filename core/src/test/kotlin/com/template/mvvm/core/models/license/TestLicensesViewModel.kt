package com.template.mvvm.core.models.license

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import com.template.mvvm.core.generateLicenseList
import com.template.mvvm.core.models.registerLifecycleOwner
import com.template.mvvm.core.sleepWhile
import com.template.mvvm.repository.contract.LicensesDataSource
import io.kotlintest.properties.Gen
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.mockito.Mockito.`when` as mockWhen

@RunWith(RobolectricTestRunner::class)
class TestLicensesViewModel {
    @Mock
    private lateinit var application: Application
    @Mock
    private lateinit var dataSource: LicensesDataSource
    @Mock
    private lateinit var lifeOwner: LifecycleOwner
    private lateinit var lifecycle: LifecycleRegistry
    private lateinit var vm: SoftwareLicensesViewModel

    @Before
    fun setUp() {
        //MockitoAnnotations.initMocks(this)
        application = Mockito.mock(Application::class.java)
        dataSource = Mockito.mock(LicensesDataSource::class.java)
        lifeOwner = Mockito.mock(LifecycleOwner::class.java)
        lifecycle = LifecycleRegistry(lifeOwner)
        mockWhen(lifeOwner.lifecycle).thenReturn(lifecycle)
        vm = SoftwareLicensesViewModel(application, dataSource)
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun testOnBoundIndexOutOfBoundsException() {
        vm.onBound(Gen.negativeIntegers().generate())
    }

    @Test
    fun testOnBound() = runBlocking {
        val size = Gen.choose(0, 200).generate()
        mockWhen(
            dataSource.getAllLibraries(
                CommonPool
            )
        ).thenReturn(produce(CommonPool) { send(generateLicenseList(size).generate()) })

        vm.registerLifecycleOwner(lifeOwner)
        vm.controller.libraryItemVmList.observeForever {
            vm.onBound(0)
        }
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        sleepWhile {
            vm.controller.libraryListSource?.value?.size != size
        }

        MatcherAssert.assertThat(
            vm.controller.libraryListSource?.value?.size,
            CoreMatchers.equalTo(size)
        )
    }

}