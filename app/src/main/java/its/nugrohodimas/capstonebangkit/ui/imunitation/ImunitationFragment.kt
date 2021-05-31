package its.nugrohodimas.capstonebangkit.ui.imunitation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import its.nugrohodimas.capstonebangkit.databinding.FragmentImunitationBinding
import its.nugrohodimas.capstonebangkit.ui.MainAdapter

class ImunitationFragment : Fragment() {

    private lateinit var dashboardViewModel: ImunitationViewModel
    private var _binding: FragmentImunitationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel =
            ViewModelProvider(this).get(ImunitationViewModel::class.java)

        _binding = FragmentImunitationBinding.inflate(inflater, container, false)

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Jadwal Imunisasi"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Riwayat"))
        binding.tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter =
            MainAdapter(container!!.context, requireFragmentManager(), binding.tabLayout.tabCount)
        binding.viewPager.adapter = adapter
        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}