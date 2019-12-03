package co.ke.lakehub.oterider.viewmodel_factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import co.ke.lakehub.oterider.viewmodels.OrderViewModel

class OrderViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
            return OrderViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}