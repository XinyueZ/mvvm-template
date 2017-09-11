package com.template.mvvm.home

import android.app.Activity
import android.arch.lifecycle.LifecycleFragment
import android.content.Intent
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.template.mvvm.R
import com.template.mvvm.actor.Interactor
import com.template.mvvm.actor.Message
import com.template.mvvm.databinding.ActivityHomeBinding
import com.template.mvvm.home.msg.OpenProducts
import com.template.mvvm.life.LifeActivity
import com.template.mvvm.products.ProductsActivity

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
    override fun obtainViewModelView() = (supportFragmentManager.findFragmentById(R.id.contentFrame) ?: HomeFragment.newInstance(application)) as LifecycleFragment

    lateinit var binding: ActivityHomeBinding
    override fun setViewDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivityHomeBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerOnActor()
    }

    private fun registerOnActor() {
        Interactor.start(this)
                .subscribe(OpenProducts::class, this::openProducts)
                .subscribeError(this::onActorError)
                .register()
    }

    private fun openProducts(msg: Message<Any>) {
        ProductsActivity.showInstance(this)
    }

    private fun onActorError(e: Throwable) {
        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
    }
}