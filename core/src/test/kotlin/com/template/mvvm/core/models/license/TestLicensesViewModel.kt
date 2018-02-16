package com.template.mvvm.core.models.license

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.template.mvvm.repository.contract.LicensesDataSource
import com.template.mvvm.repository.domain.licenses.Library
import com.template.mvvm.repository.domain.licenses.License
import io.kotlintest.matchers.shouldEqual
import io.kotlintest.properties.Gen
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.notNullValue
import org.hamcrest.Matchers.nullValue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when` as mockWhen

@Ignore
class TestLicensesViewModel {
    @Mock
    private lateinit var application: Application
    @Mock
    private lateinit var dataSource: LicensesDataSource
    @Mock
    private lateinit var lifecycle: Lifecycle

    private lateinit var vm: SoftwareLicensesViewModel

    @Before
    fun setUp() {
        application = Mockito.mock(Application::class.java)
        dataSource = Mockito.mock(LicensesDataSource::class.java)

        lifecycle = Mockito.mock(Lifecycle::class.java)
        mockWhen(lifecycle.currentState).thenReturn(Lifecycle.State.INITIALIZED)

        vm = SoftwareLicensesViewModel(application, dataSource)
    }

    @Test
    fun testOnBound() {
        runBlocking {
            val lifeThing = Mockito.mock(LifecycleOwner::class.java)
            mockWhen(lifeThing.lifecycle).thenReturn(lifecycle)
            vm.registerLifecycle(lifeThing)

            launch(UI) {
                val size = Gen.positiveIntegers().generate()
                val readyToGenerate = generateLicenseList(size).generate()
                mockWhen(
                    dataSource.getAllLibraries(
                        coroutineContext,
                        Gen.bool().generate()
                    )
                ).thenReturn(
                    produce(coroutineContext) {
                        send(readyToGenerate)
                    })
                vm.onBound(0)
                assertThat(
                    vm.libraryItemVmList.value,
                    `is`(nullValue())
                )
                assertThat(
                    vm.libraryItemVmList.value,
                    `is`(notNullValue())
                )
                readyToGenerate.shouldEqual(vm.libraryItemVmList.value)
            }.join()
        }
    }

    private fun generateLicenseList(size: Int) = object : Gen<List<Library>> {
        override fun generate(): List<Library> = mutableListOf<Library>().apply {
            for (i in 0 until size) {
                add(
                    Library(
                        Gen.string().generate(),
                        Gen.string().generate(),
                        Gen.string().generate(),
                        License(Gen.string().generate(), Gen.string().generate())
                    )
                )
            }
        }
    }
}