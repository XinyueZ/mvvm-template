package com.template.mvvm.about

import android.app.Application
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.template.mvvm.BuildConfig
import com.template.mvvm.R
import com.template.mvvm.life.LifeViewModel

class AboutViewModel(app: Application) : LifeViewModel(app) {
    val title = ObservableInt(R.string.about_title)
    val versionTitle = ObservableField<String>()
    val versionContent = ObservableField<String>()
    val descriptionTitle = ObservableField<String>()
    val descriptionContent = ObservableField<String>()

    init {
        with(app) {
            versionTitle.set(getString(R.string.about_version_title))
            versionContent.set(getString(R.string.about_version_content, BuildConfig.VERSION_NAME))
            descriptionTitle.set(getString(R.string.about_description_title))
            descriptionContent.set(getString(R.string.about_description_content))
        }
    }

    //Return this view to home
    val goBack = ObservableBoolean(false)

    fun toggleBack() {
        goBack.set(true)
    }
}