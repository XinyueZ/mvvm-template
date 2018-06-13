package com.template.mvvm.core.models.about

import androidx.databinding.BaseObservable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import com.template.mvvm.core.R

class AboutViewModelState : BaseObservable() {
    //Return this view to home
    val goBack = ObservableBoolean(false)

    val title = ObservableInt(R.string.about_title)
    val versionTitle = ObservableInt(R.string.about_version_title)
    val versionContent = ObservableInt(R.string.about_version_content)
    val descriptionTitle = ObservableInt(R.string.about_description_title)
    val descriptionContent = ObservableInt(R.string.about_description_content)
}
