package com.example.guruveda
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.guruveda.DataModel.VideoModel
import com.example.guruveda.ViewModel.VideoGetViewModel
import com.example.guruveda.databinding.ActivityCourseDetailBinding

class CourseDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCourseDetailBinding
    private lateinit var adapter: VideoAdapter
    private lateinit var list: ArrayList<VideoModel>
    private lateinit var viewModel: VideoGetViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val description = intent.getStringExtra("description")
        val title = intent.getStringExtra("title")
        val price = intent.getStringExtra("price")
        val videoCount = intent.getStringExtra("videoCount")
        val thumbnail = intent.getStringExtra("thumbnail")


        binding.descriptionDetail.text = description
        binding.titleDetail.text = title
        binding.priceDetail.text = price
        binding.videoCountText.text = videoCount
        Glide.with(this).load(thumbnail).into(binding.courseImageDetail)

        list = ArrayList()
        adapter = VideoAdapter(this, list)
        binding.videoRecyclerView.adapter = adapter
        binding.videoRecyclerView.layoutManager = LinearLayoutManager(this)

        viewModel = ViewModelProvider(this)[VideoGetViewModel::class.java]
        viewModel.videoLiveData.observe(this) {
            list.clear()
            list.addAll(it)
            adapter.notifyDataSetChanged()
        }
        val courseId = intent.getStringExtra("courseId")

        if (courseId != null) {
            viewModel.getVideos(courseId)
        }

    }
}