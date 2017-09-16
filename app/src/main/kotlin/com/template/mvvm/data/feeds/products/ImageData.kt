package com.template.mvvm.data.feeds.products

import com.google.gson.annotations.SerializedName

data
class ImageData
(
        @SerializedName("thumbnailHdUrl") val thumbnailHdUrl: String,
        @SerializedName("largeHdUrl") val largeHdUrl: String
)