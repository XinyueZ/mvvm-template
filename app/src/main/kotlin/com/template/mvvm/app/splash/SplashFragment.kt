package com.template.mvvm.app.splash

import android.content.Context
import android.databinding.ViewDataBinding
import android.support.v4.app.Fragment
import android.view.View
import com.template.mvvm.app.AppBaseFragment
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.FragmentSplashBinding
import com.template.mvvm.core.models.splash.SplashViewModel

class SplashFragment : AppBaseFragment<SplashViewModel>() {
    companion object {
        fun newInstance(cxt: Context) = Fragment.instantiate(cxt, SplashFragment::class.java.name) as SplashFragment
    }

    private lateinit var binding: FragmentSplashBinding

    override fun bindingView(view: View): ViewDataBinding {
        binding = FragmentSplashBinding.bind(view)
                .apply {
                    vm = obtainViewModel().apply {
                        activity?.let {
                            registerLifecycle(it)
                        }
                    }
                }
        return binding
    }

    override fun getLayout() = R.layout.fragment_splash
    override fun requireViewModel() = SplashViewModel::class.java
}