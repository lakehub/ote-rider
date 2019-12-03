package co.ke.lakehub.oterider.retrofit

import co.ke.lakehub.oterider.serializers.*
import kotlinx.coroutines.Deferred
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface RetrofitService {
    @GET("geocode/json")
    fun geoCodeAsync(@Query("address") address: String, @Query("key") apiKey: String,
                     @Query("region") region: String)
            : Deferred<Response<GeoCodingResponseSerializer>>

    @POST("user/rider/auth/login")
    fun signInAsync(@Header("Authorization") auth: String): Deferred<Response<SignInSerializer>>

    @POST("user/rider")
    fun signUpAsync(@Body params: RequestBody): Deferred<Response<CommonPostResponseSerializer>>

    @GET("directions/json")
    fun getDirectionsAsync(@Query("origin") origin: String, @Query("destination") destination: String,
                           @Query("waypoints") waypoints: String, @Query("key") apiKey: String,
                           @Query("region") region: String)
            : Deferred<Response<DirectionsResponseSerializer>>

    @POST("user/fcm")
    fun sendDeviceIdAsync(@Header("Authorization") auth: String, @Body params: RequestBody)
            : Deferred<Response<CommonResponseSerializer>>

    @POST("user/location")
    fun updateDeviceLocationAsync(@Header("Authorization") auth: String, @Body params: RequestBody)
            : Deferred<Response<CommonResponseSerializer>>

    @POST("order/{ORDER_ID}")
    fun acceptOrderAsync(@Header("Authorization") auth: String, @Path("ORDER_ID") orderId: Int)
            : Deferred<Response<AcceptOrderResponseSerializer>>

}