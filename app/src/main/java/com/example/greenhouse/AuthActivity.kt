package com.example.greenhouse

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.greenhouse.databinding.ActivityAuthBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

class AuthActivity : AppCompatActivity() {
    lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.activity_auth)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeVisibility(intent.getStringExtra("status").toString())

        binding.goSignInBtn.setOnClickListener {    // 회원 가입 Button
            changeVisibility("signin")
        }

        binding.signBtn.setOnClickListener {    // 가입 Button
            val email = binding.authEmailEditView.text.toString() //사용자가 입력한 email 가져옴.
            val password = binding.authPasswordEditView.text.toString() //사용자가 입력한 password 가져옴.
            MyApplication.auth.createUserWithEmailAndPassword(email,password) //createUserWithEmailAndPassword: 입력받은 값을 가지고 email과 password 만듦
                .addOnCompleteListener(this){task ->
                    //입력 내용 지우기
                    binding.authEmailEditView.text.clear()
                    binding.authPasswordEditView.text.clear()

                    if(task.isSuccessful){ //잘 만들어진 경우
                        MyApplication.auth.currentUser?.sendEmailVerification() //회원가입 사용자에게 이메일 전송(사용자 이메일에서 확인 버튼)
                            ?.addOnCompleteListener{sendTask ->
                                if(sendTask.isSuccessful){ //메일발송 성공, 회원가입 성공
                                    Toast.makeText(baseContext,"회원가입 성공!!.. 메일을 확인해주세요",Toast.LENGTH_SHORT).show()
                                    Log.d("mobileapp", "회원가입 성공!!")
                                    changeVisibility("logout") //회원가입만 성공..아직 로그아웃 상태
                                }
                                else{
                                    Toast.makeText(baseContext,"메일발송 실패",Toast.LENGTH_SHORT).show()
                                    Log.d("mobileapp", "메일발송 실패")
                                    changeVisibility("logout")
                                }
                            }
                    }
                    else{ //회원가입 실패
                        Toast.makeText(baseContext,"회원가입 실패",Toast.LENGTH_SHORT).show()
                        Log.d("mobileapp", "== ${task.exception} ==")
                        changeVisibility("logout")
                    }
                }
        }

        binding.loginBtn.setOnClickListener {   // 로그인 Button
            val email = binding.authEmailEditView.text.toString()
            val password = binding.authPasswordEditView.text.toString()
            MyApplication.auth.signInWithEmailAndPassword(email,password) //signInWithEmailAndPassword: 로그인에 필요한 함수
                .addOnCompleteListener(this){task ->
                    binding.authEmailEditView.text.clear()
                    binding.authPasswordEditView.text.clear()

                    if(task.isSuccessful){ //로그인 페이지에서 입력된 email과 password가 등록되어 있음을 확인.
                        if(MyApplication.checkAuth()){ //등록 확인된 user가 인증받은 user인지 확인
                            MyApplication.email = email
                            Log.d("mobileapp", "로그인 성공")
                            finish()
                        }
                        else{ //email이 등록은 됐지만 email 받아 인증을 하지 않은 경우
                            Toast.makeText(baseContext,"이메일 인증이 되지 않았습니다.",Toast.LENGTH_SHORT).show()
                            Log.d("mobileapp", "이메일 인증 안됨")
                        }
                    }
                    else{ //로그인 실패
                        Toast.makeText(baseContext,"로그인 실패",Toast.LENGTH_SHORT).show()
                        Log.d("mobileapp", "로그인 실패")
                    }
                }
        }

        binding.logoutBtn.setOnClickListener {  // 로그아웃 Button
            MyApplication.auth.signOut() //auth 객체에 대해 signOut()
            NaverIdLoginSDK.logout() //네이버 로그아웃
            MyApplication.email = null //이전에 로그인한 email 남지 않도록 null로 지워줌.
            Log.d("mobileapp", "로그 아웃")
            finish()
        }


        // [구글 인증 로그인]

        val requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            Log.d("mobileapp","account1 : ${task.toString()}")
            //Log.d("mobileapp","account2 : ${task.result}")
            try{
                val account = task.getResult(ApiException::class.java)
                val crendential = GoogleAuthProvider.getCredential(account.idToken, null) //처음에는 email로 로그인, 이후에는 crendential을 이용해 자동 로그인
                MyApplication.auth.signInWithCredential(crendential)
                    .addOnCompleteListener(this){task ->
                        if(task.isSuccessful){
                            MyApplication.email = account.email //로그인한 구글 email을 MyApplication의 email에 저장하여 사용
                            Toast.makeText(baseContext,"구글 로그인 성공",Toast.LENGTH_SHORT).show()
                            Log.d("mobileapp", "구글 로그인 성공")
                            finish()
                        }
                        else{
                            changeVisibility("logout")
                            Toast.makeText(baseContext,"구글 로그인 실패",Toast.LENGTH_SHORT).show()
                            Log.d("mobileapp", "구글 로그인 실패")
                        }
                    }
            }catch (e: ApiException){ // APIException은 이미 지정된 exception말고 custom한 exception을 만들어서 쓰고 싶을때 사용
                changeVisibility("logout")
                Toast.makeText(baseContext,"구글 로그인 Exception : ${e.printStackTrace()},${e.statusCode}",Toast.LENGTH_SHORT).show()
                Log.d("mobileapp", "구글 로그인 Exception : ${e.message}, ${e.statusCode}")
            }
        }

        binding.googleLoginBtn.setOnClickListener { // 구글인증 Button
            val gso = GoogleSignInOptions // 구글 로그인에 필요한 것 설정
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN) //기본
                .requestIdToken(getString(R.string.default_client_id)) //요청 id 전달
                .requestEmail()
                .build()
            val signInIntent = GoogleSignIn.getClient(this,gso).signInIntent //앞서 설정한 설정에 맞게 구글 로그인 화면(하나의 프로그램) 띄움.
            requestLauncher.launch(signInIntent) //requestLauncher: 실행 후 return값 있는 경우
        }


        // [네이버 인증 로그인]
        binding.naverLoginBtn.setOnClickListener {
            val oAuthLoginCallback = object :OAuthLoginCallback {
                override fun onSuccess() {
                    //네이버 로그인 API 호출 성공 시 유저 정보를 가져옴
                    NidOAuthLogin().callProfileApi(object:NidProfileCallback<NidProfileResponse>{
                        override fun onSuccess(result: NidProfileResponse) {
                            MyApplication.email = result.profile?.email.toString()
                            finish()
                        }

                        override fun onError(errorCode: Int, message: String) {
                            Log.d("mobileapp", message)
                        }

                        override fun onFailure(httpStatus: Int, message: String) {
                            Log.d("mobileapp", message)
                        }
                    })
                }
                override fun onError(errorCode: Int, message: String) {
                    Log.d("mobileapp", message)
                }
                override fun onFailure(httpStatus: Int, message: String) {
                    Log.d("mobileapp", message)
                }
            }
            NaverIdLoginSDK.initialize(this, getString(R.string.naver_client_id), getString(R.string.naver_client_secret), "green_house")
            NaverIdLoginSDK.authenticate(this, oAuthLoginCallback)
        }
    }

    fun changeVisibility(mode:String){ //String 형태로 mode(로그인, 로그아웃,회원가입) 받음.
        if(mode.equals("login")){       // 현재 로그인 상태 -> 로그인 상태에서 인증키를 누름 -> 로그아웃 -> 로그아웃에 필요한 요소만 VISIBLE하게!
            binding.run{
                authMainTextView.text = "정말 로그아웃하시겠습니까?"
                authMainTextView.visibility = View.VISIBLE
                logoutBtn.visibility = View.VISIBLE
                goSignInBtn.visibility = View.GONE
                authEmailEditView.visibility = View.GONE
                authPasswordEditView.visibility = View.GONE
                signBtn.visibility = View.GONE
                loginBtn.visibility= View.GONE
                googleLoginBtn.visibility = View.GONE
                naverLoginBtn.visibility = View.GONE
            }
        }
        else if(mode.equals("logout")){ // 현재 로그아웃 상태 -> 로그인 혹은 회원가입 -> 로그인, 회원가입에 필요한 요소만 VISIBLE
            binding.run{
                authMainTextView.text = "로그인 하거나 회원가입 해주세요."
                authMainTextView.visibility = View.VISIBLE
                logoutBtn.visibility = View.GONE
                goSignInBtn.visibility = View.VISIBLE //회원 가입 페이지 이동 버튼
                authEmailEditView.visibility = View.VISIBLE
                authPasswordEditView.visibility = View.VISIBLE
                signBtn.visibility = View.GONE //정보 입력 후 회원가입하기 위한 버튼
                loginBtn.visibility= View.VISIBLE
                googleLoginBtn.visibility = View.VISIBLE
                naverLoginBtn.visibility = View.VISIBLE
            }
        }
        else if(mode.equals("signin")){    // 회원가입 버튼 클릭 : 회원가입 진행 상태
            binding.run{
                authMainTextView.visibility = View.GONE
                logoutBtn.visibility = View.GONE
                goSignInBtn.visibility = View.GONE
                authEmailEditView.visibility = View.VISIBLE
                authPasswordEditView.visibility = View.VISIBLE
                signBtn.visibility = View.VISIBLE
                loginBtn.visibility= View.GONE
                googleLoginBtn.visibility = View.GONE
                naverLoginBtn.visibility = View.GONE
            }
        }
    } //changeVisibility()
}