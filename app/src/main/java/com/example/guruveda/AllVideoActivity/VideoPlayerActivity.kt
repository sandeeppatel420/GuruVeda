package com.example.guruveda.AllVideoActivity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.media3.exoplayer.ExoPlayer
import com.example.guruveda.ViewModel.VideoPlayerViewModel
import com.example.guruveda.databinding.ActivityVideoPlayerBinding

class VideoPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideoPlayerBinding
    private var player: ExoPlayer? = null
    private val viewModel: VideoPlayerViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videoUrl = intent.getStringExtra("videoUrl")
        val title = intent.getStringExtra("title")
        binding.videoTitle.text = title


        viewModel.isLoading.observe(this, Observer {
            binding.loadingProgress.visibility = if (it) View.VISIBLE else View.GONE
        })

       if (!videoUrl.isNullOrEmpty()){
           viewModel.initializePlayer(videoUrl)
       }
        if (viewModel.player != null) {
            binding.playerView.player = viewModel.player
        }    }

    override fun onStop() {
        super.onStop()
        player?.release()
        player = null

    }
}