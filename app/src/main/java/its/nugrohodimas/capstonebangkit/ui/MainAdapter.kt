package its.nugrohodimas.capstonebangkit.ui

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import its.nugrohodimas.capstonebangkit.ui.imunitation.detail.HistoryFragment
import its.nugrohodimas.capstonebangkit.ui.imunitation.detail.ScheduleImunitationFragment

@Suppress("DEPRECATION")
internal class MainAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int
) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                ScheduleImunitationFragment()
            }
            1 -> {
                HistoryFragment()
            }
            else -> getItem(position)
        }
    }
}