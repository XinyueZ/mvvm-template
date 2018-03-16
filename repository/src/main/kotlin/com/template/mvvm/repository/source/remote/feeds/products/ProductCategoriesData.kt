package com.template.mvvm.repository.source.remote.feeds.products

import com.google.gson.annotations.SerializedName

data
class ProductCategoriesData
(
        @SerializedName("categories") val products: List<ProductCategoryData>
)
