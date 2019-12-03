package co.ke.lakehub.oterider.services

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleService
import co.ke.lakehub.oterider.app.MainApplication
import co.ke.lakehub.oterider.network_requests.updateLocation
import co.ke.lakehub.oterider.utils.NotificationMngr.Companion.displayNotification
import co.ke.lakehub.oterider.utils.UPDATE_LOCATION_NOTIFICATION_ID
import com.google.android.gms.location.*

class UpdateLocationService: LifecycleService() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var lat = 0.0
    private var lng = 0.0
    val handler = Handler()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(MainApplication.applicationContext())
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                lat = locationResult.lastLocation.latitude
                lng = locationResult.lastLocation.longitude
            }
        }
        requestLocationUpdates()
        handler.postDelayed(object : Runnable {
            override fun run() {
                    updateLocation(lat, lng)

                handler.postDelayed(this, 30000)
            }
        }, 0)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        startForeground(UPDATE_LOCATION_NOTIFICATION_ID, displayNotification())

    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }
}