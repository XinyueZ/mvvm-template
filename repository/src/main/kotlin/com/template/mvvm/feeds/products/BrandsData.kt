package com.template.mvvm.feeds.products

import com.google.gson.annotations.SerializedName

data class BrandsData (
        @SerializedName("content") val brands: List<BrandData>
)
