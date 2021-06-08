package its.nugrohodimas.capstonebangkit.ui.imunitation.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import its.nugrohodimas.capstonebangkit.databinding.FragmentScheduleImunitationBinding
import its.nugrohodimas.capstonebangkit.ui.adapter.VaccAdapter
import its.nugrohodimas.capstonebangkit.ui.imunitation.ImunitationViewModel
import its.nugrohodimas.core.data.source.local.entity.VaccineEntity
import its.nugrohodimas.core.data.source.local.room.KiaDatabase
import org.json.JSONException
import org.json.JSONObject
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.IOException
import java.nio.charset.Charset

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ScheduleImunitationFragment : Fragment() {

    private var _binding: FragmentScheduleImunitationBinding? = null

    private val binding get() = _binding!!

    private val imunitationViewModel: ImunitationViewModel by viewModel()

    private val dataList = ArrayList<VaccineEntity>()
    private var date = ArrayList<VaccineEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleImunitationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Thread {
            val db =
                Room.databaseBuilder(requireContext(), KiaDatabase::class.java, "Health").build()
            date = db.kiaDao().getAllVaccine() as ArrayList<VaccineEntity>
            if (date.isEmpty()) {
                try {
                    val obj = JSONObject(loadJSONFromAsset())
                    val userArray = obj.getJSONArray("vaccine")
                    for (i in 0 until userArray.length()) {
                        val vaccine = userArray.getJSONObject(i)
                        val list = VaccineEntity(
                            idVaccine = vaccine.getInt("id"),
                            vaccineFunction = vaccine.getString("vaccineFunc"),
                            vaccineName = vaccine.getString("name"),
                            vaccineIndication =  vaccine.getString("indication"),
                            vaccinePostEvent =  vaccine.getString("postImmunization")

                        )
                        dataList.add(list)
                    }
                    db.kiaDao().insertListVaccine(dataList)
                    val linearLayoutManager = LinearLayoutManager(context)
                    binding.rvVaccine.layoutManager = linearLayoutManager
                    val customAdapter = VaccAdapter(dataList)
                    binding.rvVaccine.adapter = customAdapter
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                val linearLayoutManager = LinearLayoutManager(context)
                binding.rvVaccine.layoutManager = linearLayoutManager
                val customAdapter = VaccAdapter(date)
                binding.rvVaccine.adapter = customAdapter
            }
            Log.d("Test data DB", date.toString())
        }.start()

        val linearLayoutManager = LinearLayoutManager(context)
        binding.rvVaccine.layoutManager = linearLayoutManager
        val customAdapter = VaccAdapter(dataList)
        binding.rvVaccine.adapter = customAdapter
    }

    private fun loadJSONFromAsset(): String {
        val json: String?
        try {
            val inputStream = context?.assets?.open("vaccine.json")
            val size = inputStream?.available()
            val buffer = size?.let { ByteArray(it) }
            val charset: Charset = Charsets.UTF_8
            inputStream?.read(buffer)
            inputStream?.close()
            json = buffer?.let { String(it, charset) }
        } catch (ex: IOException) {
            ex.printStackTrace()
            return ""
        }
        return json.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}