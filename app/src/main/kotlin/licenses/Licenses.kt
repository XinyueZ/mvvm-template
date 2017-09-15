package licenses

import com.google.gson.annotations.SerializedName

data class Licenses(
        @SerializedName("Licenses")
        var licenses: List<License>
)
