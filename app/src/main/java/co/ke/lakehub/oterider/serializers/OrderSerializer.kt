package co.ke.lakehub.oterider.serializers

import com.google.gson.annotations.SerializedName

class OrderSerializer {
    @SerializedName("orderId")
    var orderId: Int? = null

    @SerializedName("deliveryAddress")
    var deliveryAddress: String? = null

    @SerializedName("pickupAddress")
    var pickupAddress: String? = null

    @SerializedName("pickupLat")
    var pickupLat: Double? = null

    @SerializedName("pickupLng")
    var pickupLng: Double? = null

    @SerializedName("deliveryLat")
    var deliveryLat: Double? = null

    @SerializedName("deliveryLng")
    var deliveryLng: Double? = null

    @SerializedName("duration")
    var duration: Long? = null

    @SerializedName("distance")
    var distance: Long? = null

    @SerializedName("good")
    var good: String? = null

    @SerializedName("date")
    var date: String? = null

    @SerializedName("fee")
    var fee: Int? = null
}