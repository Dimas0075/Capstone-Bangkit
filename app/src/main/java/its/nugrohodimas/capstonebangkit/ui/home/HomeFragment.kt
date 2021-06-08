package its.nugrohodimas.capstonebangkit.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.google.firebase.firestore.*
import its.nugrohodimas.capstonebangkit.R
import its.nugrohodimas.capstonebangkit.databinding.FragmentHomeBinding
import its.nugrohodimas.capstonebangkit.ui.adapter.ArticleAdapter
import its.nugrohodimas.core.data.source.local.entity.VaccineEntity
import its.nugrohodimas.core.data.source.local.room.KiaDatabase
import its.nugrohodimas.core.domain.model.Article

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private lateinit var articleList: ArrayList<Article>
    private lateinit var articleAdapter: ArticleAdapter
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val binding get() = _binding!!
    private var data = VaccineEntity()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.rvHomeInformation.layoutManager = GridLayoutManager(context, 2)
        binding.rvHomeInformation.setHasFixedSize(true)
        articleList = arrayListOf()
        articleAdapter = ArticleAdapter(articleList)
        binding.rvHomeInformation.adapter = articleAdapter

        Thread {
            try {
                val database =
                    Room.databaseBuilder(requireContext(), KiaDatabase::class.java, "Health").build()
                data = database.kiaDao().getUpcomingVaccine()[0]
                Log.d("Test Data Home", data.toString())

                requireActivity().runOnUiThread {
                    binding.tvHomeVaccineName.text = data.vaccineName
                    binding.tvHomeDateAndTime.text = "${data.date} - ${data.time}"
                    binding.tvHomeHospital.text = data.hospital
                }
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }.start()
        setUpRecycler()
        return binding.root
    }

    private fun setUpRecycler() {
        db.collection("article")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("Test", "Listen failed.", e)
                    return@addSnapshotListener
                }

                for (doc in value!!) {
                    val article = Article(
                        doc.getString("author")!!,
                        doc.getString("content")!!,
                        doc.getString("date")!!,
                        doc.getString("id_article")!!,
                        doc.getString("title")!!,
                        doc.getString("image")!!
                    )
                    articleList.add(article)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}