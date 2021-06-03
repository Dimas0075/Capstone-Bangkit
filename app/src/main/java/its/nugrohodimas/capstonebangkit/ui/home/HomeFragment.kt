package its.nugrohodimas.capstonebangkit.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import its.nugrohodimas.capstonebangkit.databinding.FragmentHomeBinding
import its.nugrohodimas.core.domain.model.Article
import its.nugrohodimas.core.domain.model.Vaccine

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val dataList: MutableList<Article> = mutableListOf()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.rvHomeInformation.layoutManager = GridLayoutManager(context, 2)
        val db = FirebaseFirestore.getInstance()
        db.collection("article").get()
            .addOnSuccessListener {

            }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}