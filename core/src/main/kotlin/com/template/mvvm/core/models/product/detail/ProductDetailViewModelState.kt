package com.template.mvvm.core.models.product.detail

import androidx.databinding.BaseObservable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.databinding.ObservableList
import androidx.databinding.ObservableLong
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
    val productId = ObservableLong()
    val productTitle = ObservableField<String>()
    val productDescription = ObservableField<Spanned>()
    val productImageUris: ObservableList<Uri> = ObservableArrayList()
    //------------------------------------------------------------------------
}