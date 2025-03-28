package com.example.guruveda

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.guruveda.Fragment.CourseFragment
import com.example.guruveda.Fragment.HomeFragment
import com.example.guruveda.Fragment.ProfileFragment
import com.example.guruveda.Fragment.TestFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNav : BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottomNav)
        loadFragment(HomeFragment())
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home_icon -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.card_icon-> {
                    loadFragment(TestFragment())
                    true
                }
                R.id.myOrder_icon-> {
                    loadFragment(CourseFragment())
                    true
                }
                R.id. profile_icon-> {
                    loadFragment(ProfileFragment())
                    true
                }

                else -> {
                    return@setOnItemSelectedListener false
                }
            }
        }
    }

    private  fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout_container, fragment)
        transaction.commit()
    }
}
