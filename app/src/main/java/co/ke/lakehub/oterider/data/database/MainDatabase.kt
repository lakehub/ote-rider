package co.ke.lakehub.oterider.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import co.ke.lakehub.oterider.app.MainApplication
import co.ke.lakehub.oterider.data.dao.OrderDao
import co.ke.lakehub.oterider.data.entities.Order

@Database(
        entities = [Order::class],
        version = 1, exportSchema = false
)
abstract class MainDatabase : RoomDatabase() {
    abstract val orderDao: OrderDao

    companion object {
        private var INSTANCE: MainDatabase? = null

        fun get(): MainDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room
                        .databaseBuilder(
                                MainApplication.applicationContext(),
                                MainDatabase::class.java,
                                "main_db"
                        )
                        .build()
            }

            return INSTANCE!!
        }
    }

}
