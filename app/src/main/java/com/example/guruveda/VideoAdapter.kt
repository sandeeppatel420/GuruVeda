package com.example.guruveda

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.guruveda.DataModel.VideoModel
import com.example.guruveda.ViewModel.VideoGetViewModel
import com.example.guruveda.databinding.VideoItemBinding

class VideoAdapter(val context: Context,val list: List<VideoModel>): RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {
    class VideoViewHolder(val binding: VideoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val videoImage = binding.videoImage
        val videoTitle = binding.videoTitle
        val videoDuration = binding.videoDuration
        val videoType = binding.videoType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoAdapter.VideoViewHolder {
       val binding = VideoItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoAdapter.VideoViewHolder, position: Int) {
       val data = list[position]
        holder.videoTitle.text = data.title
        holder.videoDuration.text = data.duration
        holder.videoType.text = data.type
        Glide.with(context).load(data.videoUrl).into(holder.videoImage)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, CourseDetailActivity::class.java)
            intent.putExtra("courseId", data.id)
            intent.putExtra("title", data.title)
            context.startActivity(intent)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, VideoPlayerActivity::class.java)
            intent.putExtra("videoUrl", data.videoUrl)
            intent.putExtra("title", data.title)
            context.startActivity(intent)
        }


        }




    override fun getItemCount(): Int {
       return list.size
    }
}