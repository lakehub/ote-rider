package co.ke.lakehub.oterider.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import co.ke.lakehub.oterider.data.entities.Order

@Dao
interface OrderDao: BaseDao<Order> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItem(item: Order)

    @Update
    fun updateItem(item: Order)

    @Query("SELECT * FROM orders WHERE id = :orderId")
    fun getItem(orderId: Int): LiveData<Order?>

    @Query("SELECT * FROM orders")
    fun getItems(): LiveData<List<Order>>
}