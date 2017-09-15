package licenses

import com.google.gson.annotations.SerializedName

data class License(
        @SerializedName("name")
        var name: String,
        @SerializedName("description")
        var description: String,
        @SerializedName("libraries")
        var libraries: List<Library>
)
