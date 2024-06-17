package com.example.greenhouse

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.greenhouse.ItemData
import com.example.greenhouse.databinding.ItemMyplantBinding

class MyPlantViewHolder(val binding: ItemMyplantBinding) : RecyclerView.ViewHolder(binding.root)

class MyPlantAdapter (val context: Context, val itemList: MutableList<ItemData>): RecyclerView.Adapter<MyPlantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPlantViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyPlantViewHolder(ItemMyplantBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    override fun onBindViewHolder(holder: MyPlantViewHolder, position: Int) {
        val data = itemList.get(position)

        holder.binding.run {
            PNickName.text = data.pnickname
            PType.text = data.ptype
            Pbirthday.text = data.start_date
        }

        val imageRef = MyApplication.storage.reference.child("images/${data.docId}.jpg")
        imageRef.downloadUrl.addOnCompleteListener { task ->
            if(task.isSuccessful){
                holder.binding.itemImageView.visibility = View.VISIBLE
                Glide.with(context)
                    .load(task.result)
                    .into(holder.binding.itemImageView)
            }

        }

    }
}

