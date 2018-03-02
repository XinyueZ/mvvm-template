package com.template.mvvm.app.about

import android.view.View
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.FragmentAboutBinding
import com.template.mvvm.base.ui.ViewModelFragment
import com.template.mvvm.core.get
import com.template.mvvm.core.models.about.AboutViewModel

class AboutFragment : ViewModelFragment<AboutViewModel>() {

    override fun onViewCreated(view: View) = FragmentAboutBinding.bind(view).apply {
        requestViewModel().get(this@AboutFragment) {
            vm = this
        }
    }

    override fun getLayout() = R.layout.fragment_about
}