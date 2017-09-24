package com.template.mvvm.feeds.products

import android.net.Uri
import com.google.gson.annotations.SerializedName

data
class BrandData
(
        @SerializedName("name") val name: String,
        @SerializedName("logoUrl") val logo: Uri?
)