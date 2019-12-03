package co.ke.lakehub.oterider.serializers

import com.google.gson.annotations.SerializedName

class AcceptOrderResponseSerializer {
    @SerializedName("error")
    var error: Boolean? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("order")
    var order: OrderSerializer? = null
}