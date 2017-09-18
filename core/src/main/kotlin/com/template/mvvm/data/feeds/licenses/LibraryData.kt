package com.template.mvvm.data.feeds.licenses

import com.google.gson.annotations.SerializedName

data
class LibraryData(
        @SerializedName("name")
        var name: String,
        @SerializedName("owner")
        var owner: String,
        @SerializedName("copyright")
        var copyright: String
)
