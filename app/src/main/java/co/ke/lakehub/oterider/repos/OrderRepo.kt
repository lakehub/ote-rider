package co.ke.lakehub.oterider.repos

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import co.ke.lakehub.oterider.data.dao.OrderDao
import co.ke.lakehub.oterider.data.database.MainDatabase
import co.ke.lakehub.oterider.data.entities.Order

class OrderRepo {
    private val db = MainDatabase.get()
    private val dao = db.orderDao

    fun getItem(orderId: Int): LiveData<Order?> {
        return dao.getItem(orderId)
    }

    fun getItems(): LiveData<List<Order>> {
        return dao.getItems()
    }

    fun addItem(distance: Order) {
        InsertAsync(dao).execute(distance)
    }

    fun deleteItem(distance: Order) {
        DeleteAsyncTask(dao).execute(distance)
    }

    companion object {
        private class InsertAsync internal constructor(private val mDao: OrderDao) :
            AsyncTask<Order, Void, Void>() {
            override fun doInBackground(vararg params: Order): Void? {
                mDao.addItem(params[0])
                return null
            }

        }

        private class DeleteAsyncTask internal constructor(private val mDao: OrderDao) :
            AsyncTask<Order, Void, Void>() {

            override fun doInBackground(vararg params: Order): Void? {
                mDao.delete(params[0])
                return null
            }
        }
    }
}