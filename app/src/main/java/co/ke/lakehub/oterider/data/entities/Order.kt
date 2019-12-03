package co.ke.lakehub.oterider.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "date")
    var date: String = "",
    @ColumnInfo(name = "driverDispatchDate")
    var driverDispatchDate: String = "",
    @ColumnInfo(name = "deliveryAddress")
    var deliveryAddress: String = "",
    @ColumnInfo(name = "pickupAddress")
    var pickupAddress: String = "",
    @ColumnInfo(name = "pickupLat")
    var pickupLat: Double = 0.0,
    @ColumnInfo(name = "pickupLng")
    var pickupLng: Double = 0.0,
    @ColumnInfo(name = "deliveryLat")
    var deliveryLat: Double = 0.0,
    @ColumnInfo(name = "deliveryLng")
    var deliveryLng: Double = 0.0,
    @ColumnInfo(name = "duration")
    var duration: Long = 0,
    @ColumnInfo(name = "distance")
    var distance: Long = 0,
    @ColumnInfo(name = "fee")
    var fee: Int = 0,
    @ColumnInfo(name = "status")
    var status: Int = 2,
    @ColumnInfo(name = "good")
    var good: String = ""
)