package com.template.mvvn.home

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.template.mvvn.R
import com.template.mvvn.databinding.FragmentHomeBinding
import com.template.mvvn.life.LifeFragment

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.bind(root).apply {
            vm = (activity as HomeActivity).obtainViewModel() as HomeViewModel
        }
        return binding.root
    }
}