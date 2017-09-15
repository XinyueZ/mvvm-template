package com.template.mvvm.home

import android.app.Activity
import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.template.mvvm.R
import com.template.mvvm.about.AboutActivity
import com.template.mvvm.actor.Interactor
import com.template.mvvm.actor.Message
import com.template.mvvm.databinding.ActivityHomeBinding
import com.template.mvvm.ext.replaceFragmentInActivity
import com.template.mvvm.ext.setup
import com.template.mvvm.home.msg.OpenAbout
import com.template.mvvm.home.msg.OpenItem
import com.template.mvvm.home.msg.OpenProducts
import com.template.mvvm.life.LifeActivity
import com.template.mvvm.products.ProductsActivity
import licenses.LicensesActivity

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
    override fun obtainViewModelView() = (supportFragmentManager.findFragmentById(R.id.contentFrame) ?: Item1Fragment.newInstance(application)) as LifecycleFragment

    lateinit var binding: ActivityHomeBinding
    override fun setViewDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivityHomeBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerOnActor()
        hideSystemUi(0)

        setUpHomeNavi()

    }

    private fun setUpHomeNavi() {
        with((obtainViewModel() as HomeViewModel).drawerSubViewModel) {
            binding.drawer.setup(this@HomeActivity, drawerToggle)
            openProduct.observe(this@HomeActivity, Observer {
                openProducts(OpenProducts("open product"))
            })
            openLicenses.observe(this@HomeActivity, Observer {
                LicensesActivity.showInstance(this@HomeActivity)
            })
            openAbout.observe(this@HomeActivity, Observer {
                openAbout(OpenAbout("open about"))
            })
            openItem1.observe(this@HomeActivity, Observer {
                openItem(OpenItem(1))
            })
            openItem2.observe(this@HomeActivity, Observer {
                openItem(OpenItem(2))
            })
            openItem3.observe(this@HomeActivity, Observer {
                openItem(OpenItem(3))
            })
        }
    }

    private fun registerOnActor() {
        //NO NEED more , use LiveData to drive opening stages.
        Interactor.start(this)
                .subscribe(OpenProducts::class, this::openProducts)
                .subscribe(OpenAbout::class, this::openAbout)
                .subscribe(OpenItem::class, this::openItem)
                .subscribeError(this::onActorError)
                .register()
    }

    private fun onActorError(e: Throwable) {
        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun openProducts(msg: Message<Any>) {
        ProductsActivity.showInstance(this)
    }

    private fun openAbout(msg: Message<Any>) {
        AboutActivity.showInstance(this)
    }

    private fun openItem(msg: Message<Any>) {
        val ev = msg as OpenItem
        when (ev.getDetail().thing) {
            1 -> replaceFragmentInActivity(Item1Fragment.newInstance(this), R.id.contentFrame)
            2 -> replaceFragmentInActivity(Item2Fragment.newInstance(this), R.id.contentFrame)
            3 -> replaceFragmentInActivity(Item3Fragment.newInstance(this), R.id.contentFrame)
        }
    }
}