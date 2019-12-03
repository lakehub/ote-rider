package co.ke.lakehub.oterider.activities

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import co.ke.lakehub.oterider.R
import co.ke.lakehub.oterider.app.AppPreferences
import co.ke.lakehub.oterider.data.entities.Order
import co.ke.lakehub.oterider.repos.OrderRepo
import co.ke.lakehub.oterider.retrofit.RetrofitFactory
import co.ke.lakehub.oterider.utils.limitStringLength
import kotlinx.android.synthetic.main.activity_accept_order.*
import co.ke.lakehub.oterider.utils.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.ote.otedeliveries.utils.*
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException

class AcceptOrderActivity : AppCompatActivity(), OnMapReadyCallback {
    private var statusBarHeight = 0
    private var pickupLat = 0.0
    private var pickupLng = 0.0
    private var delLat = 0.0
    private var delLng = 0.0
    private var deliveryAddress = ""
    private var pickupAddress = ""
    private var mMap: GoogleMap? = null
    private var good = ""
    private var duration = 0L
    private var distance = 0L
    private var orderId = 0
    private lateinit var progressDialog: android.app.AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accept_order)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        val mapFragment: SupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        progressDialog = SpotsDialog.Builder()
            .setContext(this)
            .setTheme(R.style.SpotsDialogTheme)
            .setCancelable(false)
            .build()

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        }*/

        pickupLat = intent!!.getDoubleExtra("pickupLat", 0.0)
        pickupLng = intent.getDoubleExtra("pickupLng", 0.0)
        delLat = intent.getDoubleExtra("deliveryLat", 0.0)
        delLng = intent.getDoubleExtra("deliveryLng", 0.0)
        duration = intent.getLongExtra("duration", 0)
        distance = intent.getLongExtra("distance", 0L)
        deliveryAddress = intent.getStringExtra("deliveryAddress")!!
        pickupAddress = intent.getStringExtra("pickupAddress")!!
        good = intent.getStringExtra("good")!!
        orderId = intent.getIntExtra("orderId", 0)

        tvGood.text = good
        tvDistance.text = formatDistance(distance)
        tvDuration.text = formatDuration(duration)

        val layoutParams = cvClose.layoutParams as ConstraintLayout.LayoutParams

        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }
        layoutParams.setMargins(
            0,
            statusBarHeight.plus(dpToPx(5, this)),
            0,
            0
        )

        cvClose.layoutParams = layoutParams

        clClose.setOnClickListener {
            finish()
        }

        val handler = Handler()
        val finishHandler = Handler()
        var milliSec = 0

        finishHandler.postDelayed({
            finish()
        }, 30000)

        val myHandler = Handler()
        myHandler.postDelayed(object : Runnable {
            override fun run() {
                milliSec += 100
                if (milliSec <= 30000) {
                    myHandler.postDelayed(this, 100)
                } else {
                    handler.removeCallbacksAndMessages(null)
                    myHandler.removeCallbacksAndMessages(null)
                    finish()
                }
            }
        }, 0)

        handler.postDelayed(object : Runnable {
            override fun run() {
                val progress = milliSec.toFloat().div(30000).times(100)
                val animation =
                    ObjectAnimator.ofInt(seekBar, "progress", seekBar.progress, progress.toInt())
                animation.setDuration(100)
                    .interpolator = DecelerateInterpolator()
                animation.start()
                seekBar.clearAnimation()
                handler.postDelayed(this, 100)
            }
        }, 0)

        constraintLayout.setOnClickListener {
            finishHandler.removeCallbacksAndMessages(null)
            myHandler.removeCallbacksAndMessages(null)
            handler.removeCallbacksAndMessages(null)
            cvClose.makeGone()
            it.makeGone()
            acceptOrder()
        }

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        val uiSettings = mMap?.uiSettings
        uiSettings?.isCompassEnabled = false
        uiSettings?.isRotateGesturesEnabled = false
        uiSettings?.isTiltGesturesEnabled = true
        uiSettings?.isMyLocationButtonEnabled = true
        uiSettings?.isZoomControlsEnabled = false
        uiSettings?.isMapToolbarEnabled = false

        val pickupPlace = LatLng(pickupLat, pickupLng)

        val markerOptions = MarkerOptions().position(pickupPlace)
            .icon(
                BitmapDescriptorFactory.fromBitmap(
                    createPickupMarker(this, limitStringLength(pickupAddress, 15))
                )
            )

        mMap?.addMarker(markerOptions)

        val deliveryPlace = LatLng(delLat, delLng)
        val markerOptionsDelivery = MarkerOptions().position(deliveryPlace)
            .icon(
                BitmapDescriptorFactory.fromBitmap(
                    createDeliveryMarker(this, limitStringLength(deliveryAddress, 15))
                )
            )

        mMap?.addMarker(markerOptionsDelivery)

        val builder = LatLngBounds.Builder()
        builder.include(pickupPlace)
        builder.include(deliveryPlace)
        val bounds = builder.build()
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 180)
        mMap?.animateCamera(cameraUpdate)

        val origin = "$pickupLat,$pickupLng"
        val destination = "$delLat,$delLng"

        getDirections(origin, destination, "")
    }

    private fun getDirections(origin: String, destination: String, waypoints: String) {
//        showProgress()
        val service = RetrofitFactory.makeRetrofitServiceMaps()
        GlobalScope.launch(Dispatchers.Main) {
            val request =
                service.getDirectionsAsync(origin, destination, waypoints, API_KEY, REGION_KENYA)
            try {
                val body = request.await().body()
                val resCode = request.await().code()

                val errorBody = request.await().errorBody()
                if (resCode == 200) {
//                    clProgress.makeGone()

                    if (body?.status == STATUS_OK) {
                        val legs = body.routes?.get(0)?.legs
                        if (legs?.isNotEmpty() == true) {
                            val polylineOptions = PolylineOptions()

                            val startPlace = LatLng(pickupLat, pickupLng)
                            val endPlace = LatLng(delLat, delLng)
                            polylineOptions.add(startPlace)
                            val routes = body.routes!!
                            val overviewPolyline = routes[0].overviewPolyline
                            polylineOptions.addAll(decodePoly(overviewPolyline?.points!!))
                            polylineOptions.add(endPlace)

                            val polyline = mMap?.addPolyline(polylineOptions)!!
                            polyline.color = ContextCompat.getColor(
                                this@AcceptOrderActivity,
                                R.color.colorPrimary
                            )
                            polyline.width = 5f

                            val builder = LatLngBounds.Builder()

                            val pickupPlace = LatLng(pickupLat, pickupLng)
                            val deliveryPlace =
                                LatLng(delLat, delLng)

                            builder.include(pickupPlace)
                            builder.include(deliveryPlace)

                            for (place in decodePoly(overviewPolyline.points!!)) {
                                builder.include(place)
                            }

                            val bounds = builder.build()
                            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 30)
                            mMap!!.moveCamera(cameraUpdate)

                        }
                    } else {
                        Log.e("TAG", "errorMessage: ${body?.errorMessage}")
                    }
                } else {
//                    clProgress.makeGone()
                    if (errorBody != null) {
                        val errorData = JSONObject(errorBody.string())
                        showWarning(errorData.getString("message"))
                    }
                }

            } catch (e: Throwable) {
//                clProgress.makeGone()
                Log.d("TAG", "error: ${e.message}")
                if (e is IOException) {
                    showNetworkError()
                }
            }
        }
    }

    private fun acceptOrder() {
        progressDialog.show()
        val service = RetrofitFactory.makeRetrofitService()
        val auth = "Bearer ${AppPreferences.token}"
        GlobalScope.launch(Dispatchers.Main) {
            val request = service.acceptOrderAsync(auth, orderId)
            try {
                val body = request.await().body()
                val resCode = request.await().code()

                val errorBody = request.await().errorBody()
                if (resCode == 200) {
                    progressDialog.dismiss()

                    if (body?.error != true) {
                        showSuccess(body!!.message!!)
                        val order = body.order
                        val myOrder = Order (
                            id = order!!.orderId!!,
                            good = order.good!!,
                            fee = order.fee!!,
                            deliveryLng = order.deliveryLng!!,
                            deliveryLat = order.deliveryLat!!,
                            deliveryAddress = order.deliveryAddress!!,
                            pickupLng = order.pickupLng!!,
                            pickupLat = order.pickupLat!!,
                            pickupAddress = order.pickupAddress!!,
                            duration = order.duration!!,
                            distance = order.distance!!,
                            date = order.date!!
                        )
                        OrderRepo().addItem(myOrder)

                        finish()
                        startActivity(Intent(this@AcceptOrderActivity, HomeActivity::class.java))
                    } else {
                        showWarning(body.message!!)
                    }
                } else {
                    progressDialog.dismiss()
                    if (errorBody != null) {
                        val errorData = JSONObject(errorBody.string())
                        showWarning(errorData.getString("message"))
                    }
                }

            } catch (e: Throwable) {
                Log.d("TAG", "error: ${e.message}")
                if (e is IOException) {
                    progressDialog.dismiss()
                    showNetworkError()
                }
            }
        }
    }
}
