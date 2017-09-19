package com.template.mvvm.models

import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import com.template.mvvm.R

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

    fun toggleBack() {
        goBack.set(true)
    }
}