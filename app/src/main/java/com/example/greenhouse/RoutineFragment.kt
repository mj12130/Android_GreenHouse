package com.example.greenhouse

import android.content.Context
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context.NOTIFICATION_SERVICE
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
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

        // 알림
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions() ) {
            if (it.all { permission -> permission.value == true }) {
                noti()
            }
            else {
                Toast.makeText(requireContext(), "permission denied...", Toast.LENGTH_SHORT).show()
            }
        }

        // 각 버튼 클릭 이벤트 설정
        binding.btn1.setOnClickListener {
            handleButtonClick(binding.btn1)
            //btn1 클릭 시 알림 띄우기
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(requireContext(),"android.permission.POST_NOTIFICATIONS") == PackageManager.PERMISSION_GRANTED) {
                    noti()
                }
                else {
                    permissionLauncher.launch( arrayOf( "android.permission.POST_NOTIFICATIONS"  ) )
                }
            }
            else {
                noti()
            }

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

    fun noti(){
        val manager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder: NotificationCompat.Builder
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){     // 26 버전 이상
            val channelId="one-channel"
            val channelName="My Channel One"
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {   // 채널에 다양한 정보 설정
                description = "My Channel One Description"
                setShowBadge(true)  // 앱 런처 아이콘 상단에 숫자 배지를 표시할지 여부를 지정
                val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
                setSound(uri, audioAttributes)
                enableVibration(true)
            }
            // 채널을 NotificationManager에 등록
            manager.createNotificationChannel(channel)
            // 채널을 이용하여 builder 생성
            builder = NotificationCompat.Builder(requireContext(), channelId)
        }
        else {  // 26 버전 미만
            builder = NotificationCompat.Builder(requireContext())
        }

        // 알림의 기본 정보
        builder.run {
            setSmallIcon(R.drawable.small)
            setWhen(System.currentTimeMillis())
            setContentTitle("기상하셨군요!")
            setContentText("온실처럼 따뜻한 하루 보내세요 :)")
            setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.big))
        }

        manager.notify(11, builder.build())
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