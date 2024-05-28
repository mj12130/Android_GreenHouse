package com.example.greenhouse

import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// Dex : Dalvic Executable (64k까지)
class MyApplication : MultiDexApplication() {
    companion object{
        //Authentication
        lateinit var auth : FirebaseAuth
        var email:String? = null

//        // firestore database
//        lateinit var db : FirebaseFirestore
//
//        // firebase storage
//        lateinit var storage : FirebaseStorage

        fun checkAuth() : Boolean { //인증된 user면 true, 아니면 false
            var currentUser = auth.currentUser //현재 Auth에 등록되어 있는 user 필요
            if(currentUser != null){
                email = currentUser.email //등록된 user의 email
                return currentUser.isEmailVerified //해당 이메일이 verified 했는가 (이메일 전송받아 확인 버튼 눌렀는지)
            }
            return false
        }
    }
    override fun onCreate(){
        super.onCreate()

        auth = Firebase.auth

//        db = FirebaseFirestore.getInstance()
//
//        storage = Firebase.storage
    }
}