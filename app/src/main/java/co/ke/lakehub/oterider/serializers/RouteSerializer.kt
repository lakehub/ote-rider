package co.ke.lakehub.oterider.serializers

import co.ke.lakehub.oterider.serializers.LegSerializer
import co.ke.lakehub.oterider.serializers.OverviewPolylineSerializer
import com.google.gson.annotations.SerializedName

class RouteSerializer {
    @SerializedName("legs")
    var legs: List<LegSerializer>? = null

    @SerializedName("overview_polyline")
    var overviewPolyline: OverviewPolylineSerializer? = null
}