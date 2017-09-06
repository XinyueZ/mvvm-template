package com.template.mvvm.splash

import android.app.Activity
import android.databinding.BindingAdapter
import android.widget.FrameLayout
import com.template.mvvm.home.HomeActivity

@BindingAdapter("startHome")
fun startHome(view: FrameLayout, startHome: Boolean) {
    when (startHome) {
        true -> {
            val cxt = view.context as Activity
            HomeActivity.showInstance(cxt)
            cxt.finish()
        }
    }
}