package com.template.mvvm.home

import android.content.Context
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.View
import com.example.android.architecture.blueprints.todoapp.util.setupSnackbar
import com.template.mvvm.R
import com.template.mvvm.databinding.FragmentHomeBinding
import com.template.mvvm.life.LifeFragment

class HomeFragment : LifeFragment() {

    companion object {
        fun newInstance(cxt: Context) = Fragment.instantiate(cxt, HomeFragment::class.java.name) as HomeFragment
    }

    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm?.let {
            view.setupSnackbar(this, it.snackbarMessage, Snackbar.LENGTH_LONG)
        }
    }

    override fun bindingView(view: View): ViewDataBinding {
        binding = FragmentHomeBinding.bind(view)
                .apply {
                    vm = obtainViewModel()
                }
        return binding
    }

    override fun getLayout() = R.layout.fragment_home
    override fun obtainViewModel() = (activity as HomeActivity).obtainViewModel() as HomeViewModel
}