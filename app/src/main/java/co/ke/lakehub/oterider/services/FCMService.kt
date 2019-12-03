package co.ke.lakehub.oterider.services

import android.content.Intent
import co.ke.lakehub.oterider.activities.AcceptOrderActivity
import co.ke.lakehub.oterider.app.AppPreferences
import co.ke.lakehub.oterider.network_requests.sendToken
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        AppPreferences.deviceToken = token
        if (!token.isNotBlank() && AppPreferences.loggedIn)
            sendToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (AppPreferences.loggedIn) {
            val data = remoteMessage.data
            val myIntent = Intent(applicationContext, AcceptOrderActivity::class.java)
            myIntent.putExtra("deliveryAddress", data["deliveryAddress"])
            myIntent.putExtra("pickupAddress", data["pickupAddress"])
            myIntent.putExtra("pickupLat", data["pickupLat"]?.toDouble())
            myIntent.putExtra("pickupLng", data["pickupLng"]?.toDouble())
            myIntent.putExtra("deliveryLng", data["deliveryLng"]?.toDouble())
            myIntent.putExtra("deliveryLat", data["deliveryLat"]?.toDouble())
            myIntent.putExtra("distance", data["distance"]?.toLong())
            myIntent.putExtra("duration", data["duration"]?.toLong())
            myIntent.putExtra("good", data["good"])
            myIntent.putExtra("orderId", data["orderId"]?.toInt())
            myIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            applicationContext.startActivity(myIntent)
        }
    }
}
