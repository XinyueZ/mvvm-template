package com.template.mvvm.app.splash

import android.view.View
import com.template.mvvm.app.AppBaseFragment
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.FragmentSplashBinding
import com.template.mvvm.core.models.registerLifecycleOwner
import com.template.mvvm.core.models.splash.SplashViewModel

class SplashFragment : AppBaseFragment<SplashViewModel>() {

    override fun onViewCreated(view: View) = FragmentSplashBinding.bind(view).apply {
        vm = obtainViewModel().apply {
            registerLifecycleOwner(this@SplashFragment)
        }
    }

    override fun getLayout() = R.layout.fragment_splash
    override fun requireViewModel() = SplashViewModel::class.java
}