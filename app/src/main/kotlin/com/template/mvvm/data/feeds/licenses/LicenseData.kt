package com.template.mvvm.data.feeds.licenses

import com.google.gson.annotations.SerializedName

data class LicenseData(
        @SerializedName("name")
        var name: String,
        @SerializedName("description")
        var description: String,
        @SerializedName("libraries")
        var libraries: List<LibraryData>
)
