package com.template.mvvm.home

import android.arch.lifecycle.Observer
import android.content.Context
import android.databinding.ViewDataBinding
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.template.mvvm.AppBaseFragment
import com.template.mvvm.R
import com.template.mvvm.about.AboutActivity
import com.template.mvvm.customtabs.CustomTabConfig
import com.template.mvvm.customtabs.CustomTabUtils
import com.template.mvvm.databinding.FragmentHomeBinding
import com.template.mvvm.ext.replaceFragmentToFragment
import com.template.mvvm.licenses.SoftwareLicensesActivity
import com.template.mvvm.models.HomeViewModel
import com.template.mvvm.products.ProductsActivity

class HomeFragment : AppBaseFragment<HomeViewModel>() {

    companion object {
        fun newInstance(cxt: Context) = instantiate(cxt, HomeFragment::class.java.name) as HomeFragment
    }

    private lateinit var binding: FragmentHomeBinding

    override fun bindingView(view: View): ViewDataBinding {
        binding = FragmentHomeBinding.bind(view)
                .apply {
                    vm = obtainViewModel().apply {
                        openProduct.observe(activity, Observer {
                            ProductsActivity.showInstance(activity)
                        })
                        openInternet.observe(activity, Observer {
                            CustomTabUtils.openWeb(activity, Uri.parse(getString(R.string.internet_url)), CustomTabConfig.builder)
                        })
                        openLicenses.observe(activity, Observer {
                            SoftwareLicensesActivity.showInstance(activity)
                        })
                        openAbout.observe(activity, Observer {
                            AboutActivity.showInstance(activity)
                        })
                        openItem1.observe(activity, Observer {
                            replaceFragmentToFragment(AllBrandsFragment.newInstance(activity), R.id.childContentFrame)
                        })
                        openItem2.observe(activity, Observer {
                            replaceFragmentToFragment(MenFragment.newInstance(activity), R.id.childContentFrame)
                        })
                        openItem3.observe(activity, Observer {
                            replaceFragmentToFragment(WomenFragment.newInstance(activity), R.id.childContentFrame)
                        })
                        registerLifecycleOwner(activity)
                    }
                }
        return binding
    }


    override fun getLayout() = R.layout.fragment_home
    override fun requireViewModel() = HomeViewModel::class.java

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        replaceFragmentToFragment(AllBrandsFragment.newInstance(activity), R.id.childContentFrame)
    }
}