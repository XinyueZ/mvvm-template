package com.template.mvvm.repository.feeds.products

import com.google.gson.annotations.SerializedName

data
class ImageData
(
        @SerializedName("sizes") val sizes: Sizes
)

data class Sizes(
        @SerializedName("Small") val small: SizeType,
        @SerializedName("XLarge") val xLarge: SizeType,
        @SerializedName("Medium") val medium: SizeType,
        @SerializedName("Large") val large: SizeType,
        @SerializedName("Best") val best: SizeType,
        @SerializedName("Original") val original: SizeType,
        @SerializedName("IPhone") val iPhone: SizeType,
        @SerializedName("IPhoneSmall") val iPhoneSmall: SizeType

)

data class SizeType(
        @SerializedName("sizeName") val sizeName: String,
        @SerializedName("url") val url: String,
        @SerializedName("width") val width: Int,
        @SerializedName("height") val height: Int,
        @SerializedName("actualWidth") val actualWidth: Int,
        @SerializedName("actualHeight") val actualHeight: Int
)