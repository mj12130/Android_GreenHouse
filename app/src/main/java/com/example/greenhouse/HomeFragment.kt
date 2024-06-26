package com.example.greenhouse

import android.R
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.example.greenhouse.databinding.FragmentHomeBinding
import com.example.greenhouse.databinding.FragmentPlantSearchBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // [ Bindning 선언]
    lateinit var binding : FragmentHomeBinding

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
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // [ 설정_Shared Preference]
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
            // < 배경 색 >
        val background = sharedPreferences.getString("background", "#FBFFEE")
        binding.homeRoot.setBackgroundColor(Color.parseColor(background))

        // 식물원 배너 클릭
        binding.bannerBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:37.5694332,126.8350132")) //기본 위치 서울식물원으로
            startActivity(intent)
        }

        // 반려 식물 클리닉
        binding.plantclinicBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://mediahub.seoul.go.kr/archives/2007600"))
            startActivity(intent)
        }

        // 반려 식물의 효과
        binding.planteffectBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.lecturernews.com/news/articleView.html?idxno=89752"))
            startActivity(intent)
        }

        // 우울증 도움 반려 식물
        binding.depressionplantBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://kairosblog.co.kr/%EC%9A%B0%EC%9A%B8%EC%A6%9D-%EC%B9%98%EB%A3%8C%EC%97%90-%EB%8F%84%EC%9B%80%EB%90%98%EB%8A%94-%EC%8B%9D%EB%AC%BC-10%EA%B0%80%EC%A7%80-part-2/"))
            startActivity(intent)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        // [ 설정_Shared Preference]
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
            // < 배경 색 >
        val background = sharedPreferences.getString("background", "#FBFFEE")
        binding.homeRoot.setBackgroundColor(Color.parseColor(background))
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}