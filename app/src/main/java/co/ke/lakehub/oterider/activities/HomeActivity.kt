package co.ke.lakehub.oterider.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import co.ke.lakehub.oterider.R
import co.ke.lakehub.oterider.adapters.HomePagerAdapter
import co.ke.lakehub.oterider.app.AppPreferences
import co.ke.lakehub.oterider.app.MainApplication
import co.ke.lakehub.oterider.network_requests.sendToken
import co.ke.lakehub.oterider.services.UpdateLocationService
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*

class HomeActivity : AppCompatActivity() {
    private lateinit var homePagerAdapter: HomePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(toolbar)

        toolbarContainer.z = 30f

        homePagerAdapter = HomePagerAdapter(supportFragmentManager)
        viewPager.adapter = homePagerAdapter

        tabLayout.setupWithViewPager(viewPager)

        menu.setOnClickListener {
            openOptionMenu(it)
        }

        onlineSwitch.isOn = AppPreferences.online

        onlineSwitch.setOnToggledListener { _, isOn ->
            AppPreferences.online = isOn
            if (isOn)
                startUpdateLocationService()
            else
                stopUpdateLocationService()

        }

        if (!AppPreferences.firstTimeTokenSent)
            sendToken()
    }

    private fun openOptionMenu(v: View) {
        val popup = PopupMenu(v.context, v)
        popup.menuInflater.inflate(R.menu.menu, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.profile -> {

                }
                R.id.logout -> {

                }
                else -> {

                }
            }
            true
        }
        popup.show()
    }

    private fun startUpdateLocationService() {
        val mIntent = Intent(MainApplication.applicationContext(), UpdateLocationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            MainApplication.applicationContext().startForegroundService(mIntent)
        } else {
            MainApplication.applicationContext().startService(mIntent)
        }
    }

    private fun stopUpdateLocationService() {
        val mIntent = Intent(MainApplication.applicationContext(), UpdateLocationService::class.java)
        MainApplication.applicationContext().stopService(mIntent)
    }
}
