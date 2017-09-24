package com.template.mvvm.feeds.products

import com.google.gson.annotations.SerializedName

data
class ProductData
(
        @SerializedName("name") val name: String,
        @SerializedName("color") val color: String,
        @SerializedName("genders") val genders: List<String>,
        @SerializedName("ageGroups") val ageGroups: List<String>,
        @SerializedName("brand") val brand: BrandData,
        @SerializedName("media") val media: MediaData
)