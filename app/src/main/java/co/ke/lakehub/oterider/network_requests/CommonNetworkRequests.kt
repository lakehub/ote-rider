package co.ke.lakehub.oterider.network_requests

import co.ke.lakehub.oterider.app.AppPreferences
import co.ke.lakehub.oterider.retrofit.RetrofitFactory
import co.ke.lakehub.oterider.utils.createJsonRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun sendToken(token: String?) {
    val auth = "Bearer ${AppPreferences.token}"
    val service = RetrofitFactory.makeRetrofitService()
    val params = createJsonRequestBody(Pair("deviceId", token!!))
    GlobalScope.launch(Dispatchers.Main) {
        val request = service.sendDeviceIdAsync(auth, params)
        try {
            val response = request.await()
            val body = response.body()
            val resCode = response.code()
            if (resCode == 200) {
                if (!body?.error!!)
                    AppPreferences.firstTimeTokenSent = true
            } else {
                AppPreferences.firstTimeTokenSent = false
            }

        } catch (e: Throwable) {
            AppPreferences.firstTimeTokenSent = false
        }
    }
}

fun sendToken() {
    val auth = "Bearer ${AppPreferences.token}"
    val service = RetrofitFactory.makeRetrofitService()
    val params = createJsonRequestBody(Pair("deviceId", AppPreferences.deviceToken!!))
    GlobalScope.launch(Dispatchers.Main) {
        val request = service.sendDeviceIdAsync(auth, params)
        try {
            val response = request.await()
            val body = response.body()
            val resCode = response.code()
            if (resCode == 200) {
                if (!body?.error!!)
                    AppPreferences.firstTimeTokenSent = true
            } else {
                AppPreferences.firstTimeTokenSent = false
            }

        } catch (e: Throwable) {
            AppPreferences.firstTimeTokenSent = false
        }
    }
}

fun updateLocation(lat: Double, lng: Double) {
    val auth = "Bearer ${AppPreferences.token}"
    val service = RetrofitFactory.makeRetrofitService()
    val params = createJsonRequestBody(Pair("lat", lat), Pair("lng", lng))
    GlobalScope.launch(Dispatchers.Main) {
        val request = service.updateDeviceLocationAsync(auth, params)
        try {
            val response = request.await()
            val body = response.body()
            val resCode = response.code()
            if (resCode == 200) {
                if (!body?.error!!) {
//                    AppPreferences.firstTimeTokenSent = true
                }
            } else {
//                AppPreferences.firstTimeTokenSent = false
            }

        } catch (e: Throwable) {
//            AppPreferences.firstTimeTokenSent = false
        }
    }
}