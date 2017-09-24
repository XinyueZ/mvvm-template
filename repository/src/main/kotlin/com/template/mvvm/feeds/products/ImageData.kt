package com.template.mvvm.feeds.products

import android.net.Uri
import com.google.gson.annotations.SerializedName

data
class ImageData
(
        @SerializedName("thumbnailHdUrl") val thumbnailHdUrl: Uri,
        @SerializedName("largeHdUrl") val largeHdUrl: Uri
)