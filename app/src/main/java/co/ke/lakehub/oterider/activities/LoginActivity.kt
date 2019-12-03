package co.ke.lakehub.oterider.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.core.widget.addTextChangedListener
import co.ke.lakehub.oterider.R
import co.ke.lakehub.oterider.app.AppPreferences
import co.ke.lakehub.oterider.retrofit.RetrofitFactory
import co.ke.lakehub.oterider.utils.ROLE_RIDER
import com.ote.otedeliveries.utils.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etUsername.addTextChangedListener { text ->
            if (text?.isBlank() == true) {
                inputLayoutUsername.showRequiredError()
            } else {
                inputLayoutUsername.hideError()
            }
        }

        etPassword.addTextChangedListener { text ->
            if (text?.isBlank() == true) {
                inputLayoutPassword.showRequiredError()
            } else {
                inputLayoutPassword.hideError()
            }
        }

        btnSignIn.setOnClickListener {
            val username = etUsername.text.toString()
            val pwd = etPassword.text.toString()
            if (username.isBlank() || pwd.isBlank()) {
                if (username.isBlank())
                    inputLayoutUsername.showRequiredError()

                if (pwd.isBlank())
                    inputLayoutPassword.showRequiredError()
            } else {
                login(username, pwd)
            }
        }

        tvSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun login(username: String, password: String) {
        showProgress()
        val credentials = "$username:$password"
        val auth = "Basic ${Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)}"
        val service = RetrofitFactory.makeRetrofitService()
        GlobalScope.launch(Dispatchers.Main) {
            val request = service.signInAsync(auth)
            try {
                val body = request.await().body()
                val resCode = request.await().code()

                val errorBody = request.await().errorBody()
                if (resCode == 200) {
                    hideProgress()

                    if (body?.error != true) {
                        val details = body!!.details!!
                        if (details.role == ROLE_RIDER) {
                            AppPreferences.loggedIn = true
                            AppPreferences.token = body.token

                            AppPreferences.fullName = details.name
                            AppPreferences.email = details.email
                            AppPreferences.phoneNo = details.phoneNo
                            AppPreferences.imgUri = details.imageUri
                            finish()
                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        } else {
                            showWarning("Invalid credentials")
                        }
                    } else {
                        showWarning(body.message!!)
                    }

                    /*val teacherImgDownloadInfo = FileDownloadInfo(
                        "${Constants.PROFILE_IMAGE_URL}/${details.imageUri}",
                        details.imageUri,
                        Constants.DETAILS_DIRECTORY_NAME
                    )
                    DownloadImgAsyncTask().execute(teacherImgDownloadInfo)*/

                    /*if (body.role == ROLE_AGENT) {
                        startActivity(Intent(applicationContext, AgentDashboardActivity::class.java))
                    } else {

                    }*/
//                    finish()
                } else {
                    hideProgress()
                    if (errorBody != null) {
                        val errorData = JSONObject(errorBody.string())
                        showWarning(errorData.getString("message"))
                    }
                }

            } catch (e: Throwable) {
                Log.d("TAG", "error: ${e.message}")
                if (e is IOException) {
                    hideProgress()
                    showNetworkError()
                }
            }
        }
    }

    private fun showProgress() {
        progress.makeVisible()
        btnSignIn.makeGone()
    }

    private fun hideProgress() {
        progress.makeGone()
        btnSignIn.makeVisible()
    }
}
