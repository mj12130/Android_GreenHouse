package com.example.greenhouse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.greenhouse.databinding.FragmentMeditationBinding
import com.example.greenhouse.databinding.FragmentRoutineBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RoutineFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RoutineFragment : Fragment() {
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
        val binding = FragmentRoutineBinding.inflate(inflater, container, false)

        // 달성도 표시를 위한 변수
        var count = 0

        // 달성도에 따른 이미지 교체 함수
        fun updateImage() {
            when (count) {
                0 -> binding.image.setImageResource(R.drawable.achievesprout)
                6 -> binding.image.setImageResource(R.drawable.achievetree)
                else -> binding.image.setImageResource(R.drawable.achieveplant)
            }
        }

        // 초기 이미지 설정
        updateImage()

        // 버튼 클릭 이벤트 처리 함수
        fun handleButtonClick(button: Button) {
            if (button.text.toString() == "미완료") {
                button.text = "완료"
                button.setBackgroundResource(R.drawable.complete_button)
                count += 1
            } else {
                button.text = "미완료"
                button.setBackgroundResource(R.drawable.incomplete_button)
                count -= 1
            }
            // 이미지 업데이트
            updateImage()
        }

        // 각 버튼 클릭 이벤트 설정
        binding.btn1.setOnClickListener {
            handleButtonClick(binding.btn1)
        }
        binding.btn2.setOnClickListener {
            handleButtonClick(binding.btn2)
        }
        binding.btn3.setOnClickListener {
            handleButtonClick(binding.btn3)
        }
        binding.btn4.setOnClickListener {
            handleButtonClick(binding.btn4)
        }
        binding.btn5.setOnClickListener {
            handleButtonClick(binding.btn5)
        }
        binding.btn6.setOnClickListener {
            handleButtonClick(binding.btn6)
        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RoutineFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RoutineFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}