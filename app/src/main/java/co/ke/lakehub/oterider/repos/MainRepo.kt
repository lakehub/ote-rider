package co.ke.lakehub.oterider.repos

import android.os.AsyncTask
import co.ke.lakehub.oterider.data.database.MainDatabase


class MainRepo {
    private val db = MainDatabase.get()

    fun clearDb() {
        ClearDbAsyncTask().execute(db)
    }

    private class ClearDbAsyncTask internal
    constructor()
        : AsyncTask<MainDatabase, Void, Void>() {
        override fun doInBackground(vararg params: MainDatabase): Void? {
            params[0].clearAllTables()
            return null
        }
    }
}