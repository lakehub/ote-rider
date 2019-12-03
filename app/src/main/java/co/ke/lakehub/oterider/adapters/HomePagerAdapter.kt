package co.ke.lakehub.oterider.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import co.ke.lakehub.oterider.fragments.OrdersCompletedFragment
import co.ke.lakehub.oterider.fragments.OrdersInProgressFragment
import java.util.*

class HomePagerAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val count = 2
    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position) {
            0 -> fragment = OrdersInProgressFragment()
            1 -> fragment = OrdersCompletedFragment()
        }
        return fragment!!
    }

    override fun getCount(): Int {
        return count
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var pageTitle: String? = null
        when(position) {
            0 -> pageTitle = "Orders In Progress"
            1 -> pageTitle = "Completed Orders"
        }
        return pageTitle?.toUpperCase(Locale.getDefault())
    }
}