package com.example.greenhouse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.greenhouse.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding : ActivityMainBinding

    // DrawerLayout Toggle
    lateinit var toggle: ActionBarDrawerToggle

    lateinit var headerView : View

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

        // DrawerLayout Toggle
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawer,
            R.string.drawer_opened,
            R.string.drawer_closed
        )
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        // Drawer 메뉴
        binding.mainDrawerView.setNavigationItemSelectedListener(this)

        headerView = binding.mainDrawerView.getHeaderView(0)
        val button = headerView.findViewById<Button>(R.id.btnAuth)
        button.setOnClickListener {
            Log.d("mobile", "button.setOnClickListener")

//            val intent = Intent(this, AuthActivity::class.java)
//            if(button.text.equals("로그인")){ //현재 로그아웃 상태(버튼이 로그인을 표시하므로..)
//                intent.putExtra("status", "logout")
//            } else if(button.text.equals("로그아웃")){ // 현재 로그인 상태
//                intent.putExtra("status", "login")
//            }
//            startActivity(intent)

            binding.drawer.closeDrawers()
        }

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
    }//onCreate()


    // DrawerLayout Toggle
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    // Drawer 메뉴
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // <설정 메뉴>
//            R.id.item_setting -> {
//                Log.d("mobileapp", "설정 메뉴")
//
//                // Drawer 메뉴에서 설정(SharedPreference)
//                //val intent = Intent(this, SettingActivity::class.java)
//                //startActivity(intent)
//
//                binding.drawer.closeDrawers()
//                true
//            }
        }
        return false
    }
}