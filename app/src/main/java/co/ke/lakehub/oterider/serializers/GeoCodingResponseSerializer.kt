package co.ke.lakehub.oterider.serializers

import co.ke.lakehub.oterider.serializers.AddressComponentsSerializer
import com.google.gson.annotations.SerializedName

class GeoCodingResponseSerializer {
    @SerializedName("results")
    val results: List<AddressComponentsSerializer>? = null

    @SerializedName("status")
    val status: String? = null

    @SerializedName("error_message")
    val errorMessage: String? = null
}