package co.ke.lakehub.oterider.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import co.ke.lakehub.oterider.data.entities.Order
import co.ke.lakehub.oterider.repos.OrderRepo

class OrderViewModel(application: Application): AndroidViewModel(application) {
    private val repo = OrderRepo()

    fun getItem(orderId: Int): LiveData<Order?> {
        return repo.getItem(orderId)
    }

    fun getItems(): LiveData<List<Order>> {
        return repo.getItems()
    }

}