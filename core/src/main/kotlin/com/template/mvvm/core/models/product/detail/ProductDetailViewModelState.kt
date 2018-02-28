package com.template.mvvm.core.models.product.detail

import android.databinding.BaseObservable
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.databinding.ObservableList
import android.net.Uri
import android.text.Spanned
import com.template.mvvm.core.R

class ProductDetailViewModelState : BaseObservable() {
    val title = ObservableInt(R.string.product_list_title)
    val dataLoaded = ObservableBoolean(false)
    //Return this view to home
    val goBack = ObservableBoolean(false)
    val dataHaveNotReloaded = ObservableBoolean(true)

    //------------------------------------------------------------------------
    // Fields to update
    //------------------------------------------------------------------------
    val productId = ObservableField<Long>()
    val productTitle = ObservableField<String>()
    val productDescription = ObservableField<Spanned>()
    val productImageUris: ObservableList<Uri> = ObservableArrayList()
    //------------------------------------------------------------------------
}