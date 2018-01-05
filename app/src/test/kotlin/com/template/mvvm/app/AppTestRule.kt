package com.template.mvvm.app

import android.Manifest
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.robolectric.shadows.ShadowApplication

class AppTestRule : TestRule {
    override fun apply(base: Statement, description: Description) = TestStatement(base)

    companion object {
        class TestStatement(private val base: Statement) : Statement() {
            override fun evaluate() {
                setup()
                base.evaluate()
                tearDown()
            }

            private fun setup() {
                ShadowApplication.getInstance().apply {
                    grantPermissions(Manifest.permission.ACCESS_NETWORK_STATE)
                }
            }

            private fun tearDown() {
            }
        }
    }
}