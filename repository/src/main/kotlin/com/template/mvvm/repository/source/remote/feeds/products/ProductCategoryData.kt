package com.template.mvvm.repository.source.remote.feeds.products

import com.google.gson.annotations.SerializedName

data
class ProductCategoryData
(
        @SerializedName("id") val cid: String,
        @SerializedName("fullName") val name: String
)