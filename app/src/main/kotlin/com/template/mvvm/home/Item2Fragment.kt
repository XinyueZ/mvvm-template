package com.template.mvvm.home

import android.content.Context
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.template.mvvm.ext.setupSnackbar
import com.template.mvvm.R
import com.template.mvvm.databinding.FragmentItem2Binding
import com.template.mvvm.ext.setupToast
import com.template.mvvm.life.LifeFragment
import com.template.mvvm.vm.models.HomeViewModel

class Item2Fragment : LifeFragment() {

    companion object {
        fun newInstance(cxt: Context) = Fragment.instantiate(cxt, Item2Fragment::class.java.name) as Item2Fragment
    }

    private lateinit var binding: FragmentItem2Binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm?.let {
            view.setupSnackbar(this, it.snackbarMessage)
            view.context.setupToast(this, it.snackbarMessage)
        }
    }

    override fun bindingView(view: View): ViewDataBinding {
        binding = FragmentItem2Binding.bind(view)
                .apply {
                    vm = obtainViewModel().apply {
                        (activity as HomeActivity).binding.vm = drawerSubViewModel
                         description.set(getString(R.string.navi_menu_item_2))
                    }
                }
        return binding
    }

    override fun getLayout() = R.layout.fragment_item_2
    override fun obtainViewModel() = (activity as HomeActivity).obtainViewModel() as HomeViewModel
}