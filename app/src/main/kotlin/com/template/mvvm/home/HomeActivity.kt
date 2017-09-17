package com.template.mvvm.home

import CustomTabUtils
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.ViewDataBinding
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import com.template.mvvm.R
import com.template.mvvm.about.AboutActivity
import com.template.mvvm.databinding.ActivityHomeBinding
import com.template.mvvm.ext.replaceFragmentInActivity
import com.template.mvvm.ext.setup
import com.template.mvvm.licenses.SoftwareLicensesActivity
import com.template.mvvm.life.LifeActivity
import com.template.mvvm.life.LifeFragment
import com.template.mvvm.products.ProductsActivity
import de.immowelt.mobile.livestream.core.utils.customtab.CustomTabConfig

class HomeActivity : LifeActivity() {

    companion object {
        fun showInstance(cxt: Activity) {
            val intent = Intent(cxt, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            ActivityCompat.startActivity(cxt, intent, Bundle.EMPTY)
        }
    }

    override fun getLayout() = R.layout.activity_home
    override fun createViewModel() = HomeViewModel::class.java
    override fun obtainViewModelView() = (supportFragmentManager.findFragmentById(R.id.contentFrame) ?: Item1Fragment.newInstance(application)) as LifeFragment

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
        with((obtainViewModel() as HomeViewModel)) {
            registerLifecycleOwner(this@HomeActivity)
            with((obtainViewModel() as HomeViewModel).drawerSubViewModel) {
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