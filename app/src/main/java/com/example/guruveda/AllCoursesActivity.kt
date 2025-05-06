package com.example.guruveda

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.guruveda.DataModel.CourseModel
import com.example.guruveda.ViewModel.AllCourseViewModel
import com.example.guruveda.allAdapter.AdapterCoures
import com.example.guruveda.databinding.ActivityAllCoursesBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AllCoursesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllCoursesBinding
    private lateinit var viewModel: AllCourseViewModel
    private lateinit var adapter: AdapterCoures
    private lateinit var list: ArrayList<CourseModel>

    @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllCoursesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list = ArrayList()
        adapter = AdapterCoures(this, list)

        binding.allCoursesRecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.allCoursesRecyclerView.adapter = adapter

            binding.allCoursesTopAppBar.setNavigationOnClickListener {
                onBackPressed()
            }


        viewModel = ViewModelProvider(this)[AllCourseViewModel::class.java]

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                lifecycleScope.launch {
                    delay(500)
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

        viewModel.errorMessage.observe(this) { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.listLiveData.observe(this) {
            list.clear()
            list.addAll(it)
            adapter.notifyDataSetChanged()
        }

        viewModel.getCourse()
    }
}
