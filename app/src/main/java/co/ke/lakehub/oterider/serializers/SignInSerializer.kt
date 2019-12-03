package co.ke.lakehub.oterider.serializers

import com.google.gson.annotations.SerializedName

class SignInSerializer {
    @SerializedName("error")
    var error: Boolean? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("token")
    var token: String? = null

    @SerializedName("details")
    var details: UserDetailsSerializer? = null
}