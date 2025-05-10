package com.example.guruveda.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.guruveda.DataModel.CourseModel
import com.example.guruveda.R
import com.example.guruveda.ViewModel.AllCourseViewModel
import com.example.guruveda.allAdapter.AdapterCoures
import com.example.guruveda.databinding.ActivityAllCourseBinding

class AllCourseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllCourseBinding
    private lateinit var viewModel: AllCourseViewModel
    private lateinit var adapter: AdapterCoures
    private lateinit var list: ArrayList<CourseModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)


        list = ArrayList()
        adapter = AdapterCoures(this, list)

        binding.allCoursesRecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.allCoursesRecyclerView.adapter = adapter

        binding.allCoursesTopAppBar.setNavigationOnClickListener {
            onBackPressed()
        }


        viewModel = ViewModelProvider(this)[AllCourseViewModel::class.java]

        viewModel.courses.observe(this) {
            list.clear()
            list.addAll(it)
            adapter.notifyDataSetChanged()
        }
        viewModel.isLoading.observe(this){ loading ->
            if(loading){
                binding.progressBar.visibility= View.VISIBLE
            }
            else{
                binding.progressBar.visibility=View.GONE
            }
            viewModel.errorMessage.observe(this){ errorMsg->
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
            }



        }
        viewModel.getCourse()

    }
}