package com.template.mvvm.data.feeds.products

import com.google.gson.annotations.SerializedName

data
class ProductsData
(
        @SerializedName("content") val products: List<ProductData>
)
