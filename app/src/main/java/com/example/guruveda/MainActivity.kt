package com.example.guruveda

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.guruveda.Fragment.CourseFragment
import com.example.guruveda.Fragment.HomeFragment
import com.example.guruveda.Fragment.PdfFragment
import com.example.guruveda.Fragment.ProfileFragment
import com.example.guruveda.Fragment.TestFragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNav : ChipNavigationBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        bottomNav = findViewById(R.id.bottomNav)
        bottomNav.setItemSelected(R.id.home_icon, true)

        val selectedCourse = intent.getStringExtra("courses")
        val freeVideo = intent.getStringExtra("freeVideo")

        val homeFragment = HomeFragment()
        val bundle = Bundle()
        bundle.putString("courses", selectedCourse)
        bundle.putString("freeVideo", freeVideo)
        homeFragment.arguments = bundle

        if (intent.getBooleanExtra("showMyCourses", false)) {
            loadFragment(CourseFragment())
            bottomNav.setItemSelected(R.id.course, true)
        } else {
            loadFragment(homeFragment)
            bottomNav.setItemSelected(R.id.home_icon, true)
        }

        bottomNav.setOnItemSelectedListener { itemId ->
            val currentFragment = supportFragmentManager.findFragmentById(R.id.frameLayout_container)

            when (itemId) {
                R.id.home_icon -> {
                    if (currentFragment !is HomeFragment) loadFragment(HomeFragment())
                    true
                }

                R.id.course -> {
                    if (currentFragment !is CourseFragment) loadFragment(CourseFragment())
                    true
                }

                R.id.MyTest_icon -> {
                    if (currentFragment !is PdfFragment) loadFragment(PdfFragment())
                    true
                }

                R.id.openBook -> {
                    if (currentFragment !is TestFragment) loadFragment(TestFragment())
                    true
                }


                R.id.profile_icon -> {
                    if (currentFragment !is ProfileFragment) loadFragment(ProfileFragment())
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
    fun selectFragment(id: Int, fragment: Fragment) {
        bottomNav.setItemSelected(id, true)
        loadFragment(fragment)
    }
}
