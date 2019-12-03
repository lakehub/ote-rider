package co.ke.lakehub.oterider.fragments

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import co.ke.lakehub.oterider.R
import co.ke.lakehub.oterider.adapters.OrdersAdapter
import co.ke.lakehub.oterider.viewmodel_factories.OrderViewModelFactory
import co.ke.lakehub.oterider.viewmodels.OrderViewModel
import kotlinx.android.synthetic.main.fragment_orders_in_progress.view.*

class OrdersInProgressFragment : Fragment() {
    private lateinit var myView: View
    private lateinit var ordersViewModel: OrderViewModel
    private lateinit var ordersAdapter: OrdersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myView = inflater.inflate(R.layout.fragment_orders_in_progress, container, false)

        ordersViewModel = ViewModelProvider(
            this,
            OrderViewModelFactory(Application())
        ).get(OrderViewModel::class.java)

        ordersAdapter = OrdersAdapter(context!!)
        val myLayoutManager = LinearLayoutManager(context)
        myView.recyclerView.apply {
            adapter = ordersAdapter
            layoutManager = myLayoutManager
        }

        ordersViewModel.getItems().observe(this, Observer {
            ordersAdapter.swapData(it)
        })

        return myView
    }

}