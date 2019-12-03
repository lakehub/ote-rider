package co.ke.lakehub.oterider.serializers

import com.google.gson.annotations.SerializedName

class UserDetailsSerializer {
    @SerializedName("name")
    val name: String? = null

    @SerializedName("phoneNumber")
    val phoneNo: String? = null

    @SerializedName("email")
    val email: String? = null

    @SerializedName("imageUri")
    val imageUri: String? = null

    @SerializedName("role")
    val role: Int? = null
}