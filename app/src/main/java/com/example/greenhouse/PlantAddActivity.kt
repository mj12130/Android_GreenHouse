package com.example.greenhouse

import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.greenhouse.databinding.ActivityPlantAddBinding
import java.text.SimpleDateFormat

class PlantAddActivity : AppCompatActivity() {
    lateinit var binding : ActivityPlantAddBinding

    lateinit var uri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlantAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // [이미지 선택 버튼]
        val requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode === android.app.Activity.RESULT_OK) { //성공적으로 이미지 불러왔으면..
                // <불러온 이미지 보여주기>
                binding.addImageView.visibility = View.VISIBLE
                Glide.with(applicationContext)
                    .load(it.data?.data)
                    .override(100, 100)
                    .into(binding.addImageView)
                // <storage에 업로드하기 위한 uri 확보>
                uri = it.data?.data!!
            }
        }
        binding.uploadButton.setOnClickListener {
            // <갤러리 앱 불러와 사진 선택>
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            requestLauncher.launch(intent)
        }


        // [저장 버튼]
        binding.saveButton.setOnClickListener {//식물 추가 후 저장 버튼 클릭 시
            if(binding.inputPNicname.text.isNotEmpty()){
                if(binding.inputPType.text.isNotEmpty()){
                    // <식물 별명, 종류, 키우기 시작한(등록) 날짜 저장>

                    //firesotre에 저장할 data 만들기
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                    val data = mapOf(
                        "pnickname" to binding.inputPNicname.text.toString(),
                        "ptype" to binding.inputPType.text.toString(),
                        "start_date" to dateFormat.format(System.currentTimeMillis())
                    )

                    //firestore에 넘겨 저장
                    MyApplication.db.collection("MyPlant") //"MyPlant" 컬렉션에 저장
                        .add(data)
                        .addOnSuccessListener {
                            Toast.makeText(this, "새로운 식물 저장 완료!", Toast.LENGTH_LONG).show()
                            uploadImage(it.id) //성공적으로 저장했으면 이미지도 storage에 업로드하기 위한 함수 호출
                            finish()//성공적인 저장 -> 돌아감
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "새로운 식물 저장 실패", Toast.LENGTH_LONG).show()
                        }
                }
                else{
                    Toast.makeText(this, "식물 종류 미입력", Toast.LENGTH_LONG).show()
                }
            }
            else{
                Toast.makeText(this, "식물 별명 미입력", Toast.LENGTH_LONG).show()
            }
        }
    }
    fun uploadImage(docId: String) {
        val imageRef = MyApplication.storage.reference.child("images/${docId}.jpg")

        val uploadTask = imageRef.putFile(uri)
        uploadTask.addOnSuccessListener {
            Toast.makeText(this, "식물 이미지 업로드 성공", Toast.LENGTH_LONG).show()
        }
        uploadTask.addOnFailureListener {
            Toast.makeText(this, "식물 이미지 업로드 실패", Toast.LENGTH_LONG).show()
        }
    }
}