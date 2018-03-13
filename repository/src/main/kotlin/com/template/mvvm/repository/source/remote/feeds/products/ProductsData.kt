package com.template.mvvm.repository.source.remote.feeds.products

import com.google.gson.annotations.SerializedName

data
class ProductsData
(
        @SerializedName("metadata") val metaData: MetaData,
        @SerializedName("products") val products: List<ProductData>
)
