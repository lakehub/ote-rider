package co.ke.lakehub.oterider.app

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import co.ke.lakehub.oterider.services.UpdateLocationService
import co.ke.lakehub.oterider.utils.API_KEY
import com.google.android.libraries.places.api.Places
import net.danlew.android.joda.JodaTimeAndroid

class MainApplication : Application() {
    init {
        INSTANCE = this
    }

    override fun onCreate() {
        super.onCreate()
        AppPreferences.init(this)
        JodaTimeAndroid.init(this)
        Places.initialize(this, API_KEY)
        if (AppPreferences.online) {
            startUpdateLocationService()
        }

    }

    private fun startUpdateLocationService() {
        val mIntent = Intent(applicationContext(), UpdateLocationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            applicationContext().startForegroundService(mIntent)
        } else {
            applicationContext().startService(mIntent)
        }
    }

    companion object {

        private var INSTANCE: MainApplication? = null

        fun applicationContext(): Context {
            return INSTANCE!!.applicationContext
        }

    }
}