package com.template.mvvm

import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class AppExampleUnitTest {
    @Test
    fun testGet() {
        Mockito.mock(List::class.java).run {
            Mockito.`when`(get(0)).thenReturn("one")
            get(0).apply {
                Assert.assertTrue("one" == this@apply)
                Assert.assertTrue(this@run.isNotEmpty())
            }
            Mockito.verify(this)[0]
        }
    }

}
