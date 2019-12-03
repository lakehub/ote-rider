package co.ke.lakehub.oterider.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.ke.lakehub.oterider.R
import co.ke.lakehub.oterider.app.AppPreferences
import java.util.*

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        Timer().schedule(object : TimerTask() {
            override fun run() {
                if (AppPreferences.loggedIn) {
                    startActivity(Intent(this@LauncherActivity, HomeActivity::class.java))
                } else {
                    startActivity(Intent(this@LauncherActivity, LoginActivity::class.java))
                }
                finish()
            }
        }, 2000L)
    }
}
