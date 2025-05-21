package com.example.guruveda.allAdapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.guruveda.DataModel.VideoModel
import com.example.guruveda.R
import com.example.guruveda.ViewModel.DownloadViewModel
import com.example.guruveda.activities.DownloadActivity
import com.example.guruveda.activities.VideoPlayerActivity
import com.example.guruveda.databinding.VideoItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

class VideoAdapter(
    val context: Context,
    private val list: MutableList<VideoModel>,
    val downloadViewModel: DownloadViewModel
) :
    RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    class VideoViewHolder(binding: VideoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val videoImage = binding.videoImage
        val videoTitle = binding.videoTitle
        val videoDuration = binding.videoDuration
        val videoType = binding.videoType
        val menuIcon = binding.menuIcon
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = VideoItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val data = list[position]
        holder.videoTitle.text = data.title
        holder.videoDuration.text = data.duration
        holder.videoType.text = data.type
        Glide.with(context).load(data.videoUrl).into(holder.videoImage)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, VideoPlayerActivity::class.java)
            intent.putExtra("videoUrl", data.videoUrl)
            intent.putExtra("title", data.title)
            context.startActivity(intent)
        }

        holder.menuIcon.setOnClickListener {
            val popupMenu = PopupMenu(context, holder.menuIcon)
            popupMenu.menuInflater.inflate(R.menu.video_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_download -> {
                        downloadViewModel.downloadVideo(context, data) { success, message ->
                            CoroutineScope(Dispatchers.Main).launch {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                        true
                    }

                    else -> false
                }
            }
            popupMenu.show()


        }
    }


    override fun getItemCount(): Int = list.size
}



