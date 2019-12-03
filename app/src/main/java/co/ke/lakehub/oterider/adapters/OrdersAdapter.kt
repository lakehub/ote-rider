package co.ke.lakehub.oterider.adapters

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import co.ke.lakehub.oterider.R
import co.ke.lakehub.oterider.data.entities.Order
import co.ke.lakehub.oterider.retrofit.RetrofitFactory
import co.ke.lakehub.oterider.utils.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.ote.otedeliveries.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException

class OrdersAdapter(val context: Context) : RecyclerView.Adapter<OrdersAdapter.MyViewModel>() {

    private var data: List<Order> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewModel {
        return MyViewModel(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.order_item, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MyViewModel, position: Int) = holder.bind(data[position])

    fun swapData(data: List<Order>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class MyViewModel(itemView: View) : RecyclerView.ViewHolder(itemView),
        OnMapReadyCallback {
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvFee: TextView = itemView.findViewById(R.id.tvFee)
        private val mapView: MapView = itemView.findViewById(R.id.map)
        private lateinit var map: GoogleMap
        private var order: Order? = null

        init {
            mapView.onCreate(null)
            mapView.getMapAsync(this)
        }

        fun bind(item: Order) = with(itemView) {
            tvDate.text = item.date
            tvFee.text = currencyFormat(item.fee)
            setOnClickListener {

            }
            mapView.onResume()
            order = item
        }

        override fun onMapReady(googleMap: GoogleMap?) {
            MapsInitializer.initialize(context)
            map = googleMap ?: return

            val uiSettings = map.uiSettings

            uiSettings?.isCompassEnabled = false
            uiSettings?.isRotateGesturesEnabled = false
            uiSettings?.isTiltGesturesEnabled = false
            uiSettings?.isMyLocationButtonEnabled = false
            uiSettings?.isZoomControlsEnabled = false
            uiSettings?.isMapToolbarEnabled = false
            uiSettings?.isScrollGesturesEnabled = false
            uiSettings?.isZoomGesturesEnabled = false

            map.moveCamera(CameraUpdateFactory.zoomTo(15f))

            val origin = "${order?.pickupLat},${order?.pickupLng}"
            val destination = "${order?.deliveryLat},${order?.deliveryLng}"

            getDirections(origin, destination, "")

        }

        private fun getDirections(origin: String, destination: String, waypoints: String) {
            val service = RetrofitFactory.makeRetrofitServiceMaps()
            GlobalScope.launch(Dispatchers.Main) {
                val request = service.getDirectionsAsync(
                    origin,
                    destination,
                    waypoints,
                    API_KEY,
                    REGION_KENYA
                )
                try {
                    val body = request.await().body()
                    val resCode = request.await().code()

                    val errorBody = request.await().errorBody()
                    if (resCode == 200) {

                        if (body?.status == STATUS_OK) {
                            val legs = body.routes?.get(0)?.legs
                            if (legs?.isNotEmpty() == true) {
                                val builder = LatLngBounds.Builder()

                                val pickupPlace = LatLng(order?.pickupLat!!, order?.pickupLng!!)
                                val deliveryPlace =
                                    LatLng(order?.deliveryLat!!, order?.deliveryLng!!)
                                val polylineOptions = PolylineOptions()
                                val routes = body.routes!!
                                val overviewPolyline = routes[0].overviewPolyline
                                polylineOptions.add(pickupPlace)
                                polylineOptions.addAll(decodePoly(overviewPolyline?.points!!))
                                polylineOptions.add(deliveryPlace)

                                val polyline = map.addPolyline(polylineOptions)!!
                                polyline.color =
                                    ContextCompat.getColor(context, R.color.colorDarkLight)
                                polyline.width = 5f

                                builder.include(pickupPlace)
                                builder.include(deliveryPlace)

                                for (place in decodePoly(overviewPolyline.points!!)) {
                                    builder.include(place)
                                }

                                val bounds = builder.build()
                                val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 30)
                                map.moveCamera(cameraUpdate)

                                /*map.addMarker(
                                    MarkerOptions().position(pickupPlace)
                                        .icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(context, R.drawable.ic_truck)))
                                        .title("marker")
                                )*/

                            }
                        } else {
                            Log.e("TAG", "errorMessage: ${body?.errorMessage}")
                        }
                    } else {
                        if (errorBody != null) {
                            val errorData = JSONObject(errorBody.string())
                        }
                    }

                } catch (e: Throwable) {
                    if (e is IOException) {
                    }
                }
            }
        }
    }
}