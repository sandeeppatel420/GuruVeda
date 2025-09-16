package com.user.guruveda.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.user.guruveda.DataModel.VideoModel
import com.user.guruveda.ViewModel.DownloadViewModel
import com.user.guruveda.ViewModel.FreeVideoGet
import com.user.guruveda.allAdapter.VideoAdapter
import com.user.guruveda.databinding.ActivityFreeVideoBinding

class FreeVideoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFreeVideoBinding
    private lateinit var adapterVideo: VideoAdapter
    private lateinit var list: ArrayList<VideoModel>
    private lateinit var viewModel: FreeVideoGet
    private lateinit var downloadViewModel: DownloadViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFreeVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        list = ArrayList()
        downloadViewModel = ViewModelProvider(this)[DownloadViewModel::class.java]
        adapterVideo = VideoAdapter(this, list,downloadViewModel)
        binding.freeVideoRecyclerView.adapter = adapterVideo
        binding.freeVideoRecyclerView.layoutManager = LinearLayoutManager(this)

        viewModel = ViewModelProvider(this)[FreeVideoGet::class.java]
        viewModel.videoLiveDataFree.observe(this) {
            list.clear()
            list.addAll(it!!)
            adapterVideo.notifyDataSetChanged()


        }
        intent.getStringExtra("title")


        viewModel.getVideos()


    }
}