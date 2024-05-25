package com.example.greenhouse

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.greenhouse.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

    // [viewpager adapter에 대한 class]
    class MyFragmentPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity){
        // Fragment를 리스트로 가져옴
        val fragments : List<Fragment>
        init{
            fragments = listOf(PlantSearchFragment(), MyPlantFragment(), HomeFragment(), RoutineFragment(), MeditationFragment()) //fragments 초기화
        }

        // override 필수: getItemCount, createFragment
        override fun getItemCount(): Int {
            return fragments.size //Fragment 갖고 있는 리스트의 길이 return
        }
        override fun createFragment(position: Int): Fragment { //몇번째 Fragment인지 position이라는 매개변수를 통해 전달.
            //position: 0=PlantSearchFragment, 1=MyPlantFragment... 와 같이 매개변수 position에 맞는 Fragment return 해주는 함수.
            return fragments[position]
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewpager.adapter = MyFragmentPagerAdapter(this) // viewpager 이용해 Fregment activity에 포함시키기 위해 adapter 사용.

        // [탭 레이아웃 추가]_탭을 이용해 viewpager 좀 더 편리하게
        TabLayoutMediator(binding.tabs, binding.viewpager){ //TabLayoutMediator 사용! - binding에서 탭 레이아웃과 뷰페이저 찾아서 묶음.
                tab, position -> // tab과 tab의 순서 전달됨.
            when(position) {
                0 -> tab.text = "식물 도감"
                1 -> tab.text = "나의 식물"
                2 -> tab.text = "홈"
                3 -> tab.text = "루틴"
                4 -> tab.text = "명상"
            }
        }.attach() // tab 버튼 부착
    }
}