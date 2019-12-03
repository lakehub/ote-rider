package co.ke.lakehub.oterider.serializers

import co.ke.lakehub.oterider.serializers.LocationAltSerializer
import com.google.gson.annotations.SerializedName

class SnappedPointSerializer {
    @SerializedName("location")
    var location: LocationAltSerializer? = null

    @SerializedName("placeId")
    var placeId: String? = null

    @SerializedName("originalIndex")
    var originalIndex: Int? = null
}