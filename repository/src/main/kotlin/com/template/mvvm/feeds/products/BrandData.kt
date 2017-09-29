package com.template.mvvm.feeds.products

import android.net.Uri
import com.google.gson.annotations.SerializedName

data
class BrandData
(
        @SerializedName("key") val key: String,
        @SerializedName("name") val name: String,
        @SerializedName("logoUrl") val logoDefault: Uri?,
        @SerializedName("logoLargeUrl") val logo: Uri?,
        @SerializedName("shopUrl") val shop: Uri?
)