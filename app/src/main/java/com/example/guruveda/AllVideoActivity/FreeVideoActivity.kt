package com.example.guruveda

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guruveda.DataModel.VideoModel
import com.example.guruveda.ViewModel.FreeVideoGet
import com.example.guruveda.allAdapter.VideoAdapter
import com.example.guruveda.databinding.ActivityFreeVideoBinding

class FreeVideoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFreeVideoBinding
    private lateinit var adapterVideo: VideoAdapter
    private lateinit var list: ArrayList<VideoModel>
    private lateinit var viewModel: FreeVideoGet


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFreeVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list = ArrayList()
        adapterVideo = VideoAdapter(this, list)
        binding.freeVideoRecyclerView.adapter = adapterVideo

        binding.freeVideosTopAppBar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.freeVideoRecyclerView.layoutManager = LinearLayoutManager(this)

        viewModel = ViewModelProvider(this)[FreeVideoGet::class.java]
        viewModel.videoLiveDataFree.observe(this) {
            list.clear()
            list.addAll(it!!)
            adapterVideo.notifyDataSetChanged()


        }
        viewModel.isLoading.observe(this) { loading ->
            if (loading) {
                binding.progressBar2.visibility = View.VISIBLE
            } else {
                binding.progressBar2.visibility = View.GONE
                }
        }
        viewModel.errorMessage.observe(this) { errorMsg ->
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()

        }

        intent.getStringExtra("title")


            viewModel.getVideos()


    }
}