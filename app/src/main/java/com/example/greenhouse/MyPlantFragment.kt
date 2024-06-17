package com.example.greenhouse

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.greenhouse.databinding.FragmentMyPlantBinding
import com.google.firebase.firestore.Query
import java.io.BufferedReader
import java.io.File
import java.io.OutputStreamWriter
import java.nio.Buffer
import java.text.SimpleDateFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyPlantFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyPlantFragment : Fragment() {
    // [ Bindning 선언]
    lateinit var binding : FragmentMyPlantBinding

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
        binding = FragmentMyPlantBinding.inflate(inflater, container, false)

        // [ 설정_Shared Preference]
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
            // < 배경 색 >
        val background = sharedPreferences.getString("background", "#FBFFEE")
        binding.myPlantRoot.setBackgroundColor(Color.parseColor(background))
            // < 글씨 색>
        val textcolor = sharedPreferences.getString("textcolor", "#ffffff")
        binding.ghname.setTextColor(Color.parseColor(textcolor))
            // < 온실 이름 내용 >
        val ghname = sharedPreferences.getString("ghname", "나의 식물")
        binding.ghname.text = ghname
            // < 온실 이름 크기 >
        val size = sharedPreferences.getString("size", "20.0")
        binding.ghname.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size!!.toFloat())



        // 파일에 저장한 값 읽어 표시
        val file = File(requireContext().filesDir, "wRecordFile.txt")
        val readStream : BufferedReader = file.reader().buffered()
        binding.waterRecord.text = "마지막으로 물 준 기록: " + readStream.readLine()

        // [물주기 기록 버튼]
        binding.wRecordButton.setOnClickListener {
            // 파일에 저장
            val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm") //마지막으로 물 준 "년-월-일 시:분" 기록
            val file = File(requireContext().filesDir, "wRecordFile.txt")
            val writeStream:OutputStreamWriter = file.writer()
            writeStream.write(dateFormat.format(System.currentTimeMillis()))
            writeStream.flush()

            // 파일에 저장한 값 읽어 표시
            val readStream : BufferedReader = file.reader().buffered()
            binding.waterRecord.text = "마지막으로 물 준 기록: " + readStream.readLine()

        }

        // [식물 추가 버튼]
        binding.addPlantButton.setOnClickListener {
            if(MyApplication.checkAuth()){//인증되어 있는 경우 식물 추가 화면(PlantAddActivity)로 이동
                startActivity(Intent(context, PlantAddActivity::class.java))
            }
            else{
                Toast.makeText(context, "식물을 추가하려면 로그인", Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }

    override fun onStart() { //PlantAddActivity에서 돌아온 후.. firestore에 저장된 값 받아옴
        super.onStart()

        if(MyApplication.checkAuth()){
            MyApplication.db.collection("MyPlant")
                .orderBy("start_date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener {result ->
                    val itemList = mutableListOf<ItemData>()
                    for(document in result){
                        val item = document.toObject(ItemData::class.java)
                        item.docId = document.id
                        itemList.add(item)
                    }
                    binding.myPlantRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                    binding.myPlantRecyclerView.adapter = MyPlantAdapter(requireContext(), itemList)
                    binding.myPlantRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
                }
                .addOnFailureListener {
                    Toast.makeText(context, "서버 데이터 획득 실패", Toast.LENGTH_LONG).show()
                }
        }
    }

    override fun onResume() {
        super.onResume()

        // [ 설정_Shared Preference]
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
            // < 배경 색 >
        val background = sharedPreferences.getString("background", "#FBFFEE")
        binding.myPlantRoot.setBackgroundColor(Color.parseColor(background))
            // < 글씨 색>
        val textcolor = sharedPreferences.getString("textcolor", "#ffffff")
        binding.ghname.setTextColor(Color.parseColor(textcolor))
        binding.addPlantButton.setTextColor(Color.parseColor(textcolor))
            // < 온실 이름 >
        val ghname = sharedPreferences.getString("ghname", "나의 식물")
        binding.ghname.text = ghname
            // < 온실 이름 크기 >
        val size = sharedPreferences.getString("size", "20.0")
        binding.ghname.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size!!.toFloat())
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyPlantFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyPlantFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}