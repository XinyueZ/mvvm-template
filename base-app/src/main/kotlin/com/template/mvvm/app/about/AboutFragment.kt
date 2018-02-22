package com.template.mvvm.app.about

import android.view.View
import com.template.mvvm.app.AppBaseFragment
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.FragmentAboutBinding
import com.template.mvvm.core.models.about.AboutViewModel

class AboutFragment : AppBaseFragment<AboutViewModel>() {

    override fun onViewCreated(view: View) = FragmentAboutBinding.bind(view).apply {
        vm = obtainViewModel()
    }

    override fun getLayout() = R.layout.fragment_about
    override fun requireViewModel() = AboutViewModel::class.java
}