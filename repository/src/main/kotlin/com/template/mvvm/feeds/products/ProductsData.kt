package com.template.mvvm.feeds.products

import com.google.gson.annotations.SerializedName

data
class ProductsData
(
        @SerializedName("content") val products: List<ProductData>
)
