package co.ke.lakehub.oterider.serializers

import co.ke.lakehub.oterider.serializers.SnappedPointSerializer
import com.google.gson.annotations.SerializedName

class SnapToRoadResponseSerializer {
    @SerializedName("snappedPoints")
    var snappedPoints: List<SnappedPointSerializer>?  = null
}