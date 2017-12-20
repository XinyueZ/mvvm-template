package com.template.mvvm.home

import android.arch.lifecycle.Observer
import android.content.Context
import android.databinding.ViewDataBinding
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import com.template.mvvm.AppBaseFragment
import com.template.mvvm.R
import com.template.mvvm.about.AboutActivity
import com.template.mvvm.customtabs.CustomTabConfig
import com.template.mvvm.customtabs.CustomTabUtils
import com.template.mvvm.databinding.FragmentHomeBinding
import com.template.mvvm.ext.replaceFragmentToFragment
import com.template.mvvm.licenses.SoftwareLicensesActivity
import com.template.mvvm.models.home.HomeViewModel
import com.template.mvvm.products.ProductsActivity

class HomeFragment : AppBaseFragment<HomeViewModel>() {

    companion object {
        fun newInstance(cxt: Context) = instantiate(cxt, HomeFragment::class.java.name) as HomeFragment
    }

    private lateinit var binding: FragmentHomeBinding

    private var menFrg: MenFragment? = null
    private var womenFrg: WomenFragment? = null
    private var allFrg: AllGendersFragment? = null
    override fun bindingView(view: View): ViewDataBinding {
        binding = FragmentHomeBinding.bind(view)
                .apply {
                    vm = obtainViewModel().apply {
                        activity?.let {
                            with(it) {
                                controller.run {
                                    openProduct.observe(it, Observer {
                                        ProductsActivity.showInstance(this@with)
                                    })
                                    openInternet.observe(it, Observer {
                                        CustomTabUtils.openWeb(this@with, Uri.parse(getString(R.string.internet_url)), CustomTabConfig.builder)
                                    })
                                    openLicenses.observe(it, Observer {
                                        SoftwareLicensesActivity.showInstance(this@with)
                                    })
                                    openAbout.observe(it, Observer {
                                        AboutActivity.showInstance(this@with)
                                    })
                                    openItem2.observe(it, Observer {
                                        if (menFrg == null)
                                            menFrg = MenFragment.newInstance(this@with)
                                        menFrg?.let { replaceFragmentToFragment(it, R.id.childContentFrame) }
                                    })
                                    openItem3.observe(it, Observer {
                                        if (womenFrg == null)
                                            womenFrg = WomenFragment.newInstance(this@with)
                                        womenFrg?.let { replaceFragmentToFragment(it, R.id.childContentFrame) }
                                    })
                                    openItem4.observe(it, Observer {
                                        if (allFrg == null)
                                            allFrg = AllGendersFragment.newInstance(this@with)
                                        allFrg?.let { replaceFragmentToFragment(it, R.id.childContentFrame) }
                                    })
                                }
                            }
                            registerLifecycleOwner(it)
                        }

                    }
                }
        return binding
    }

    override fun getLayout() = R.layout.fragment_home
    override fun requireViewModel() = HomeViewModel::class.java

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        replaceFragmentToFragment(MenFragment.newInstance(activity as FragmentActivity), R.id.childContentFrame)
    }
}