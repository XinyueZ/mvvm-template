package licenses

import com.google.gson.annotations.SerializedName

data
class Library(
        @SerializedName("name")
        var name: String,
        @SerializedName("owner")
        var owner: String,
        @SerializedName("copyright")
        var copyright: String
)
