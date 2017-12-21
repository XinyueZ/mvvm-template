package com.template.mvvm.app.about

import android.content.Context
import android.databinding.ViewDataBinding
import android.support.v4.app.Fragment
import android.view.View
import com.template.mvvm.app.AppBaseFragment
import com.template.mvvm.app.R
import com.template.mvvm.core.models.about.AboutViewModel
import com.template.mvvm.app.databinding.FragmentAboutBinding

class AboutFragment : AppBaseFragment<AboutViewModel>() {

    companion object {
        fun newInstance(cxt: Context) = Fragment.instantiate(cxt, AboutFragment::class.java.name) as AboutFragment
    }

    private lateinit var binding: FragmentAboutBinding

    override fun bindingView(view: View): ViewDataBinding {
        binding = FragmentAboutBinding.bind(view)
                .apply { vm = obtainViewModel() }
        return binding
    }

    override fun getLayout() = R.layout.fragment_about
    override fun requireViewModel() = AboutViewModel::class.java
}