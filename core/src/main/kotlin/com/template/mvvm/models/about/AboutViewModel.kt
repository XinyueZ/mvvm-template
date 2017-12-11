package com.template.mvvm.models.about

import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import com.template.mvvm.R
import com.template.mvvm.models.AbstractViewModel

class AboutViewModel : AbstractViewModel() {
    val title = ObservableInt(R.string.about_title)
    val versionTitle = ObservableInt()
    val versionContent = ObservableInt()
    val descriptionTitle = ObservableInt()
    val descriptionContent = ObservableInt()

    init {

        versionTitle.set(R.string.about_version_title)
        versionContent.set(R.string.about_version_content)
        descriptionTitle.set(R.string.about_description_title)
        descriptionContent.set(R.string.about_description_content)

    }

    //Return this view to home
    val goBack = ObservableBoolean(false)

    //-----------------------------------
    //BindingAdapter handler
    //-----------------------------------
    fun onCommand(id: Int) {
        when (id) {
            R.id.action_app_bar_indicator -> goBack.set(true)
        }
    }
    //-----------------------------------
}