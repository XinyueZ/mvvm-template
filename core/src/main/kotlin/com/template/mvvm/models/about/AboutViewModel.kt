package com.template.mvvm.models.about

import com.template.mvvm.R
import com.template.mvvm.models.AbstractViewModel

class AboutViewModel : AbstractViewModel() {
    val state = AboutViewModelState()


    //-----------------------------------
    //BindingAdapter handler
    //-----------------------------------
    fun onCommand(id: Int) {
        when (id) {
            R.id.action_app_bar_indicator -> state.goBack.set(true)
        }
    }
    //-----------------------------------
}