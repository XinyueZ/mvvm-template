package com.template.mvvm.home

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.ViewDataBinding
import android.net.Uri
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.ActivityCompat
import com.template.mvvm.AppBaseActivity
import com.template.mvvm.R
import com.template.mvvm.about.AboutActivity
import com.template.mvvm.customtabs.CustomTabConfig
import com.template.mvvm.customtabs.CustomTabUtils
import com.template.mvvm.databinding.ActivityHomeBinding
import com.template.mvvm.ext.*
import com.template.mvvm.licenses.SoftwareLicensesActivity
import com.template.mvvm.models.AppNavigationViewModel
import com.template.mvvm.models.HomeViewModel
import com.template.mvvm.products.ProductsActivity

class HomeActivity : AppBaseActivity<HomeViewModel>() {

    companion object {
        fun showInstance(cxt: Activity) {
            val intent = Intent(cxt, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            ActivityCompat.startActivity(cxt, intent, Bundle.EMPTY)
        }
    }

    override @LayoutRes
    fun getLayout() = R.layout.activity_home

    override fun requireViewModel() = HomeViewModel::class.java
    override fun createViewModelView() = Item1Fragment.newInstance(application)
    lateinit var binding: ActivityHomeBinding
    override fun setViewDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivityHomeBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemUi(0)
        setUpHomeNavi()
    }

    private fun setUpHomeNavi() {
        obtainViewModel().apply {
            binding.contentFrame.apply {
                setupSnackbar(this@HomeActivity, snackbarMessage)
                context.setupToast(this@HomeActivity, snackbarMessage)
            }

            registerLifecycleOwner(this@HomeActivity)
            this@HomeActivity.obtainViewModel(AppNavigationViewModel::class.java).apply {
                binding.drawer.setup(this@HomeActivity, drawerToggle)
                openProduct.observe(this@HomeActivity, Observer {
                    ProductsActivity.showInstance(this@HomeActivity)
                })
                openInternet.observe(this@HomeActivity, Observer {
                    CustomTabUtils.openWeb(this@HomeActivity, Uri.parse(getString(R.string.internet_url)), CustomTabConfig.builder)
                })
                openLicenses.observe(this@HomeActivity, Observer {
                    SoftwareLicensesActivity.showInstance(this@HomeActivity)
                })
                openAbout.observe(this@HomeActivity, Observer {
                    AboutActivity.showInstance(this@HomeActivity)
                })
                openItem1.observe(this@HomeActivity, Observer {
                    replaceFragmentInActivity(Item1Fragment.newInstance(this@HomeActivity), R.id.contentFrame)
                })
                openItem2.observe(this@HomeActivity, Observer {
                    replaceFragmentInActivity(Item2Fragment.newInstance(this@HomeActivity), R.id.contentFrame)
                })
                openItem3.observe(this@HomeActivity, Observer {
                    replaceFragmentInActivity(Item3Fragment.newInstance(this@HomeActivity), R.id.contentFrame)
                })
            }
        }
    }

    override fun onStart() {
        super.onStart()
        CustomTabUtils.warmUp(this, Uri.parse(getString(R.string.internet_url)))
    }

    override fun onStop() {
        super.onStop()
        CustomTabUtils.clean(this)
    }
}