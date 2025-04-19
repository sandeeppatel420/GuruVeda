package com.example.guruveda
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.guruveda.DataModel.CourseModel
import com.example.guruveda.ViewModel.AllCouresGet
import com.example.guruveda.databinding.ActivityAllCoursesBinding

class AllCoursesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllCoursesBinding
    private lateinit var viewModel: AllCouresGet
    private lateinit var adapter: AdapterCoures
    private lateinit var list: ArrayList<CourseModel>

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllCoursesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        list = ArrayList()
        adapter = AdapterCoures(this, list)


        binding.allCoursesRecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.allCoursesRecyclerView.adapter = adapter


        viewModel = ViewModelProvider(this)[AllCouresGet::class.java]

        viewModel.listLiveData.observe(this) {
            list.clear()
            list.addAll(it)
            adapter.notifyDataSetChanged()
        }

        viewModel.getCoures()
    }
}
