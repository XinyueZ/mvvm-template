package com.template.mvvm.app.home

import android.net.Uri
import android.support.v4.app.Fragment
import android.view.View
import com.template.mvvm.app.R
import com.template.mvvm.app.about.AboutActivity
import com.template.mvvm.app.databinding.FragmentHomeBinding
import com.template.mvvm.app.licenses.SoftwareLicensesActivity
import com.template.mvvm.app.product.AllGenderFragment
import com.template.mvvm.app.product.MenFragment
import com.template.mvvm.app.product.ProductsActivity
import com.template.mvvm.app.product.WomenFragment
import com.template.mvvm.app.product.category.CategoriesProductsFragment
import com.template.mvvm.base.customtabs.CustomTabConfig
import com.template.mvvm.base.customtabs.CustomTabUtils
import com.template.mvvm.base.ext.android.app.newInstance
import com.template.mvvm.base.ext.android.app.replaceFragmentToFragment
import com.template.mvvm.base.ext.android.app.showSingleTopActivity
import com.template.mvvm.base.ext.android.arch.lifecycle.setupObserve
import com.template.mvvm.base.ui.ViewModelFragment
import com.template.mvvm.core.get
import com.template.mvvm.core.models.home.HomeViewModel
import com.template.mvvm.core.arch.registerLifecycleOwner

class HomeFragment : ViewModelFragment<HomeViewModel>() {
    private var menFrg: Fragment? = null
    private var womenFrg: Fragment? = null
    private var allFrg: Fragment? = null
    private var catPrdFrag: Fragment? = null

    override fun onViewCreated(view: View) = FragmentHomeBinding.bind(view).apply {
        requestViewModel().get(this@HomeFragment) {
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
                        allFrg = AllGenderFragment::class.newInstance(context)
                    allFrg?.let { replaceFragmentToFragment(it, R.id.childContentFrame) }
                }
                openItem5.setupObserve(activity) {
                    if (catPrdFrag == null)
                        catPrdFrag = CategoriesProductsFragment::class.newInstance(context)
                    catPrdFrag?.let { replaceFragmentToFragment(it, R.id.childContentFrame) }
                }
            }
            registerLifecycleOwner(this@HomeFragment)
        }
    }

    override fun getLayout() = R.layout.fragment_home
}