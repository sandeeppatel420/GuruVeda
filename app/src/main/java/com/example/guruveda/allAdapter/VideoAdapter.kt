package com.example.guruveda.allAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.guruveda.DataModel.VideoModel
import com.example.guruveda.databinding.VideoItemBinding
import com.example.guruveda.activities.VideoPlayerActivity
import android.content.Intent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class VideoAdapter(val context: Context, private val list: MutableList<VideoModel>) :
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

        // ✅ Open video player on click
        holder.itemView.setOnClickListener {
            val intent = Intent(context, VideoPlayerActivity::class.java)
            intent.putExtra("videoUrl", data.videoUrl)
            intent.putExtra("title", data.title)
            context.startActivity(intent)
        }

        holder.menuIcon.setOnClickListener {
            val alertDialog = android.app.AlertDialog.Builder(context)
            alertDialog.setTitle("Delete")
            alertDialog.setMessage("Are you sure you want to delete this video?")
            alertDialog.setPositiveButton("Yes") { _, _ ->
                val db = FirebaseFirestore.getInstance()
                val storage = FirebaseStorage.getInstance()
                if (data.id != null) {
                    db.collection("videos").document(data.id)
                        .delete()
                        .addOnSuccessListener {
                            val fileRef = data.videoUrl?.let {
                                storage.getReferenceFromUrl(
                                    it
                                )
                            }


                            if (fileRef != null) {
                                fileRef.delete().addOnSuccessListener {
                                    Toast.makeText(context, "Video deleted", Toast.LENGTH_SHORT).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Storage delete failed", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                            list.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, list.size)
                        }.addOnFailureListener {
                            Toast.makeText(context, "Firestore delete failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            }
            alertDialog.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            alertDialog.show()
        }
    }

    override fun getItemCount(): Int = list.size
}
