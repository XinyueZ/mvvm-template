package com.template.mvvm.feeds.products

import com.google.gson.annotations.SerializedName

data
class ProductData
(
        @SerializedName("id") val pid: Long,
        @SerializedName("name") val name: String,
        @SerializedName("description") val description: String,
        @SerializedName("image") val image: ImageData
)