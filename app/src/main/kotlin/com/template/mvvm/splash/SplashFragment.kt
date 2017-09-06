package com.template.mvvm.splash

import android.content.Context
import android.databinding.ViewDataBinding
import android.support.v4.app.Fragment
import android.view.View
import com.template.mvvm.R
import com.template.mvvm.databinding.FragmentSplashBinding
import com.template.mvvm.life.LifeFragment

class SplashFragment : LifeFragment() {
    companion object {
        fun newInstance(cxt: Context) = Fragment.instantiate(cxt, SplashFragment::class.java.name) as SplashFragment
    }

    private lateinit var binding: FragmentSplashBinding

    override fun bindingView(view: View): ViewDataBinding {
        binding = FragmentSplashBinding.bind(view)
                .apply {
                    vm = obtainViewModel().apply { registerLifecycleOwner(this@SplashFragment) }
                }
        return binding
    }

    override fun getLayout(): Int = R.layout.fragment_splash
    override fun obtainViewModel() = (activity as SplashActivity).obtainViewModel() as SplashViewModel
}