package com.example.guruveda

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.guruveda.Fragment.CourseFragment
import com.example.guruveda.Fragment.HomeFragment
import com.example.guruveda.Fragment.ProfileFragment
import com.example.guruveda.Fragment.TestFragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNav : ChipNavigationBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)


        bottomNav = findViewById(R.id.bottomNav)
        loadFragment(HomeFragment())
        bottomNav.setOnItemSelectedListener { itemId ->
            when (itemId) {
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

                else -> false

            }


        }

        setStatusBar()
    }

    private  fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout_container, fragment)
        transaction.commit()
    }

    private fun setStatusBar() {
        val statusBarColor = ContextCompat.getColor(this, R.color.black2)
        window.statusBarColor = statusBarColor
    }
}
