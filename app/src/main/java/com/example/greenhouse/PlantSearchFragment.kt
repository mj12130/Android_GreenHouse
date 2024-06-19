package com.example.greenhouse

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.greenhouse.databinding.FragmentMeditationBinding
import com.example.greenhouse.databinding.FragmentMyPlantBinding
import com.example.greenhouse.databinding.FragmentPlantSearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PlantSearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlantSearchFragment : Fragment() {
    // [ Bindning 선언]
    lateinit var binding : FragmentPlantSearchBinding

    // [ Shared Preference]
    lateinit var sharedPreferences : SharedPreferences

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // [Fragment 바인딩]
        binding = FragmentPlantSearchBinding.inflate(inflater, container, false)

        // [ 설정_Shared Preference]
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
            // < 배경 색 >
        val background = sharedPreferences.getString("background", "#FBFFEE")
        binding.plantSearchRoot.setBackgroundColor(Color.parseColor(background))


        // [search 버튼 클릭 시]
        binding.btnSearch.setOnClickListener {
            val plant = binding.edtPlant.text.toString()

            Log.d("mobileapp", plant)

            val call: Call<XmlResponse> = RetrofitConnection.xmlNetworkService.getXmlList(
                "Kawa4DqzuVBD5YtDlSE+6Ks2ZLLbMM6vlLuZPhjVEfuue4eJ6Gr686FjFa83i1EdvZPPsr6odwjBHYF8TfxeNQ==", //일반 인증키(Decoding)
                1,
                plant,
                10,
                1,
            )

            //return된 값 처리
            call?.enqueue(object : Callback<XmlResponse> {
                override fun onResponse(call: Call<XmlResponse>, response: Response<XmlResponse>) {
                    if(response.isSuccessful){
                        Log.d("mobileApp", "$response")
                        Log.d("mobileApp", "${response.body()}")

                        // recyclerView에 보여주기: adapter 연결 / layoutManager 설정
                        binding.xmlRecyclerView.adapter = XmlAdapter(response.body()!!.body!!.items!!.item) //전달받는 mutableList의 타입 넘겨줘야 함.
                        binding.xmlRecyclerView.layoutManager = LinearLayoutManager(activity)
                    }
                }

                override fun onFailure(call: Call<XmlResponse>, t: Throwable) { // 통신 과정에서 오류
                    Log.d("mobileApp", "onFailure ${call.request()}")
                }
            })
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        // [ 설정_Shared Preference]
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
            // < 배경 색 >
        val background = sharedPreferences.getString("background", "#FBFFEE")
        binding.plantSearchRoot.setBackgroundColor(Color.parseColor(background))
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PlantSearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PlantSearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}