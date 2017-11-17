package com.template.mvvm

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.template.mvvm.source.Repository
import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CoreExampleUnitTest {
    @Mock private lateinit var repo: Repository
    @Mock private lateinit var context: Application

    private fun setupContext() {
        Mockito.`when`<Context>(context.applicationContext).thenReturn(context)
        Mockito.`when`(context.resources).thenReturn(Mockito.mock(Resources::class.java))

        val connectivityManager = Mockito.mock(ConnectivityManager::class.java)
        val networkInfo = Mockito.mock(NetworkInfo::class.java)
        Mockito.`when`(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).thenReturn(networkInfo)
        Mockito.`when`(networkInfo.isAvailable).thenReturn(true)
        Mockito.`when`(networkInfo.isConnected).thenReturn(true)

    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        setupContext()
        repo = Injection.getInstance(context).provideRepository(context)
    }

    @Test
    fun testGetLicense() {
        Assert.assertNotNull(repo)
    }
}
