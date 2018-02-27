package com.template.mvvm.app.home

import android.net.Uri
import android.view.View
import com.template.mvvm.app.R
import com.template.mvvm.app.about.AboutActivity
import com.template.mvvm.app.databinding.FragmentHomeBinding
import com.template.mvvm.app.licenses.SoftwareLicensesActivity
import com.template.mvvm.app.product.ProductsActivity
import com.template.mvvm.base.customtabs.CustomTabConfig
import com.template.mvvm.base.customtabs.CustomTabUtils
import com.template.mvvm.base.ext.android.app.newInstance
import com.template.mvvm.base.ext.android.app.replaceFragmentToFragment
import com.template.mvvm.base.ext.android.app.showSingleTopActivity
import com.template.mvvm.base.ext.android.arch.lifecycle.setupObserve
import com.template.mvvm.base.ui.LiveFragment
import com.template.mvvm.core.generateViewModel
import com.template.mvvm.core.models.home.HomeViewModel
import com.template.mvvm.core.models.registerLifecycleOwner

class HomeFragment : LiveFragment() {
    private var menFrg: MenFragment? = null
    private var womenFrg: WomenFragment? = null
    private var allFrg: AllGendersFragment? = null

    override fun onViewCreated(view: View) = FragmentHomeBinding.bind(view).apply {
        HomeViewModel::class.generateViewModel(this@HomeFragment) {
            vm = this
            controller.run {
                openProduct.setupObserve(activity) {
                    ProductsActivity::class.showSingleTopActivity(
                        activity
                    )
                }
                openInternet.setupObserve(activity) {
                    CustomTabUtils.openWeb(
                        activity,
                        Uri.parse(getString(R.string.internet_url)),
                        CustomTabConfig.builder
                    )
                }
                openLicenses.setupObserve(activity) {
                    SoftwareLicensesActivity::class.showSingleTopActivity(
                        activity
                    )
                }
                openAbout.setupObserve(activity) {
                    AboutActivity::class.showSingleTopActivity(
                        activity
                    )
                }
                openItem2.setupObserve(activity) {
                    if (menFrg == null)
                        menFrg = MenFragment::class.newInstance(context)
                    menFrg?.let { replaceFragmentToFragment(it, R.id.childContentFrame) }
                }
                openItem3.setupObserve(activity) {
                    if (womenFrg == null)
                        womenFrg = WomenFragment::class.newInstance(context)
                    womenFrg?.let { replaceFragmentToFragment(it, R.id.childContentFrame) }
                }
                openItem4.setupObserve(activity) {
                    if (allFrg == null)
                        allFrg = AllGendersFragment::class.newInstance(context)
                    allFrg?.let { replaceFragmentToFragment(it, R.id.childContentFrame) }
                }
            }
            registerLifecycleOwner(this@HomeFragment)
        }
    }

    override fun getLayout() = R.layout.fragment_home
}