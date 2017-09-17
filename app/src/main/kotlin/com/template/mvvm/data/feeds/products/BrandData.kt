package com.template.mvvm.data.feeds.products

import com.google.gson.annotations.SerializedName

data
class BrandData
(
        @SerializedName("name") val name: String,
        @SerializedName("logoUrl") val logo: String?
)