package com.example.guruveda.allAdapter

import android.R.id.list
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.guruveda.DataModel.VideoModel
import com.example.guruveda.R
import com.example.guruveda.activities.VideoPlayerActivity
import org.json.JSONObject
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator

class DownloadedVideoAdapter(private val context: Context, var videos: ArrayList<VideoModel>
) : RecyclerView.Adapter<DownloadedVideoAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val videoDownloadImage = view.findViewById<ImageView>(R.id.videoImage)
        val videoDownloadTitle = view.findViewById<TextView>(R.id.videoTitle)
        val videoDownloadDuration = view.findViewById<TextView>(R.id.videoDuration)
        val videoDownloadType = view.findViewById<TextView>(R.id.videoType)
        val menuIcon = view.findViewById<ImageView>(R.id.menuIcon)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.video_item, parent, false)
        return VideoViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videos[position]
        holder.videoDownloadTitle.text = video.title
        holder.videoDownloadType.text = video.type
        holder.videoDownloadDuration.text = video.duration
       Glide.with(context).load(video.videoUrl).into(holder.videoDownloadImage)


        holder.itemView.setOnClickListener {
            val intent = Intent(context, VideoPlayerActivity::class.java)
            intent.putExtra("videoUrl", video.videoUrl)
            intent.putExtra("videoTitle", video.title)
            intent.putExtra("videoDuration", video.duration)
            intent.putExtra("videoType", video.type)
            context.startActivity(intent)

        }
        holder.menuIcon.setOnClickListener {
            val alertDialog = android.app.AlertDialog.Builder(context)
            alertDialog.setTitle("Delete Video")
            alertDialog.setMessage("Are you sure you want to delete this video?")
            alertDialog.setPositiveButton("Yes") { dialog, _ ->
                videos.removeAt(position)
                val prefs = context.getSharedPreferences("downloads", MODE_PRIVATE).edit()
                prefs.clear()
                prefs.apply()

                for (video in videos) {
                    val videoJson = JSONObject()
                    videoJson.put("title", video.title)
                    videoJson.put("path", video.videoUrl)
                    videoJson.put("duration", video.duration)
                    videoJson.put("type", video.type)
                    prefs.putString(video.title, videoJson.toString())
                    prefs.apply()

                }

                notifyDataSetChanged()
                dialog.dismiss()
            }
            alertDialog.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            alertDialog.show()
        }



    }

    override fun getItemCount(): Int = videos.size
}
