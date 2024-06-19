package com.example.greenhouse

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.greenhouse.databinding.ItemMainBinding

class XmlViewHolder(val binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root)
class XmlAdapter(val context: Context, val datas:MutableList<myXmlItem>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {     // 1-1
    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return XmlViewHolder(ItemMainBinding.inflate(LayoutInflater.from(parent.context)))
        //return XmlViewHolder(ItemMainBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as XmlViewHolder).binding
        val model = datas!![position]

        holder.binding.run {
            familyKorNm.text = model.familyKorNm
            genusKorNm.text = model.genusKorNm
            plantGnrlNm.text = model.plantGnrlNm
            plantPilbkNo.text = model.plantPilbkNo.toString()

            val detailYn = model.detailYn.toString()

            // [이미지 처리]
            Glide.with(binding.root)
                .load(model.imgUrl) // XmlResponse에서 받은 imgUrl을 받아와
                .override(240, 120) // 이미지 크기 조정(불러온 이미지를 원하는 크기로 만들어 집어넣음)
                .into(binding.urlImage) // binding.urlImage에 넣기


            // 리사이클러 뷰 클릭_상세 정보 표시
            plantPilbkNo.setOnClickListener {
                if(detailYn == "Y") {
                    Toast.makeText(context, "상세 정보 확인", Toast.LENGTH_LONG).show()
                    Intent(context, PlantInfoActivity::class.java).apply {
                        putExtra("plantPilbkNo", plantPilbkNo.text)
                    }.run { context.startActivity(this) }
                }
                else{
                    Toast.makeText(context, "상세 정보 미제공", Toast.LENGTH_LONG).show()
                }

            }
        }
    }
}