package com.template.mvvm.home

import android.content.Context
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.example.android.architecture.blueprints.todoapp.util.setupSnackbar
import com.template.mvvm.R
import com.template.mvvm.databinding.FragmentItem3Binding
import com.template.mvvm.ext.setupToast
import com.template.mvvm.life.LifeFragment

class Item3Fragment : LifeFragment() {

    companion object {
        fun newInstance(cxt: Context) = Fragment.instantiate(cxt, Item3Fragment::class.java.name) as Item3Fragment
    }

    private lateinit var binding: FragmentItem3Binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm?.let {
            view.setupSnackbar(this, it.snackbarMessage)
            view.context.setupToast(this, it.snackbarMessage)
        }
    }

    override fun bindingView(view: View): ViewDataBinding {
        binding = FragmentItem3Binding.bind(view)
                .apply {
                    vm = obtainViewModel().apply {
                        (activity as HomeActivity).binding.vm = drawerSubViewModel
                        description.set(getString(R.string.navi_menu_item_3))
                    }
                }
        return binding
    }

    override fun getLayout() = R.layout.fragment_item_3
    override fun obtainViewModel() = (activity as HomeActivity).obtainViewModel() as HomeViewModel
}