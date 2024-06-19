package com.example.greenhouse

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.greenhouse.databinding.ActivityPlantInfoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlantInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPlantInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 리사이클러 뷰에서 도감 번호 받아오기
        val plantPilbkNo = intent.getStringExtra("plantPilbkNo")

        // 도감 번호에 맞는 상세정보 api 불러오기
        val call: Call<XmlResponse2> = RetrofitConnection.xmlNetworkService.getXmlInfo(
            "Kawa4DqzuVBD5YtDlSE+6Ks2ZLLbMM6vlLuZPhjVEfuue4eJ6Gr686FjFa83i1EdvZPPsr6odwjBHYF8TfxeNQ==", //일반 인증키(Decoding)
            plantPilbkNo!!.toInt()
        )

        //return된 값 처리
        call?.enqueue(object : Callback<XmlResponse2> {
            override fun onResponse(call: Call<XmlResponse2>, response: Response<XmlResponse2>) {
                if(response.isSuccessful){
                    Log.d("mobileApp", "$response")
                    Log.d("mobileApp", "${response.body()}")

                    // xml에 표시
                    val data = response.body()!!.body!!.item
                    binding.familyKorNm.text = data.familyKorNm
                    binding.genusKorNm.text = data.genusKorNm
                    binding.plantGnrlNm.text = data.plantGnrlNm
                    binding.shpe.text = data.shpe
                    binding.spft.text = data.spft
                    binding.grwEvrntDesc.text = data.grwEvrntDesc
                    binding.useMthdDesc.text = data.useMthdDesc
                    binding.dstrb.text = data.dstrb
                    binding.farmSpftDesc.text = data.farmSpftDesc
                    binding.brdMthdDesc.text = data.brdMthdDesc
                    binding.bugInfo.text = data.bugInfo
                    binding.bfofMthod.text = data.bfofMthod

                    // [이미지 처리]
                    Glide.with(binding.root)
                        .load(data.imgUrl)
                        .override(300, 150)
                        .into(binding.imgUrl)
                }
            }

            override fun onFailure(call: Call<XmlResponse2>, t: Throwable) { // 통신 과정에서 오류
                Log.d("mobileApp", "onFailure ${call.request()}")
            }
        })


    }
}