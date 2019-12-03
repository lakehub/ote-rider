package co.ke.lakehub.oterider.serializers

import com.google.gson.annotations.SerializedName

class CommonResponseSerializer {
    @SerializedName("error")
    var error: Boolean? = null

    @SerializedName("message")
    var message: String? = null
}