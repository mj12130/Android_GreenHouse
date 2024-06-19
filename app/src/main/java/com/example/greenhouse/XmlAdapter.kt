package com.example.greenhouse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.greenhouse.databinding.ItemMainBinding

class XmlViewHolder(val binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root)
class XmlAdapter(val datas:MutableList<myXmlItem>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {     // 1-1
    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return XmlViewHolder(ItemMainBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as XmlViewHolder).binding
        val model = datas!![position]

        binding.familyKorNm.text = model.familyKorNm
        binding.genusKorNm.text = model.genusKorNm
        binding.plantGnrlNm.text = model.plantGnrlNm

        // [이미지 처리]
        Glide.with(binding.root)
            .load(model.imgUrl) // XmlResponse에서 받은 imgUrl을 받아와
            .override(300, 150) // 이미지 크기 조정(불러온 이미지를 원하는 크기로 만들어 집어넣음)
            .into(binding.urlImage) // binding.urlImage에 넣기
    }
}