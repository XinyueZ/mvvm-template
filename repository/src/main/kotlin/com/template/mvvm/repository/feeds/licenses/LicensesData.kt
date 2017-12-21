package com.template.mvvm.repository.feeds.licenses

import com.google.gson.annotations.SerializedName

data
class LicensesData(
        @SerializedName("Licenses")
        var licenses: List<LicenseData>
)
