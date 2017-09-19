package com.template.mvvm.home

import android.content.Context
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.template.mvvm.AppBaseFragment
import com.template.mvvm.R
import com.template.mvvm.databinding.FragmentItem1Binding
import com.template.mvvm.ext.setupSnackbar
import com.template.mvvm.ext.setupToast
import com.template.mvvm.models.HomeViewModel

class Item1Fragment : AppBaseFragment<HomeViewModel>() {

    companion object {
        fun newInstance(cxt: Context) = Fragment.instantiate(cxt, Item1Fragment::class.java.name) as Item1Fragment
    }

    private lateinit var binding: FragmentItem1Binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm?.let {
            view.setupSnackbar(this, it.snackbarMessage)
            view.context.setupToast(this, it.snackbarMessage)
        }
    }

    override fun bindingView(view: View): ViewDataBinding {
        binding = FragmentItem1Binding.bind(view)
                .apply {
                    vm = obtainViewModel().apply {
                        (activity as HomeActivity).binding.vm = drawerSubViewModel
                        description.set(getString(R.string.navi_menu_item_1))
                    }
                }
        return binding
    }

    override fun getLayout() = R.layout.fragment_item_1
}