package com.example.guruveda.allAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.guruveda.CourseDetailActivity
import com.example.guruveda.DataModel.CourseModel
import com.example.guruveda.databinding.CoursesDesignBinding


class AdapterCoures(val context: Context, val list: List<CourseModel>): RecyclerView.Adapter<AdapterCoures.CourseViewHolder>() {
    class CourseViewHolder(val binding: CoursesDesignBinding) : RecyclerView.ViewHolder(binding.root){
       val courseImage = binding.courseImage
        val courseTitle = binding.courseTitle
        val coursePrice = binding.coursePrice
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
      val  binding = CoursesDesignBinding.inflate(LayoutInflater.from(context),parent,false)
        return CourseViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
       val data = list[position]
        holder.courseTitle.text = data.courseTitle
        holder.coursePrice.text = "₹ ${data.coursePrice}"
        Glide.with(context).load(data.courseThumbnail).into(holder.courseImage)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, CourseDetailActivity::class.java)
            intent.putExtra("description", "Description : ${data.courseDescription}")
            intent.putExtra("title", data.courseTitle)
            intent.putExtra("price"," price :${data.coursePrice}")
            intent.putExtra("videoCount","Total Video :  ${data.videoCount}")
            intent.putExtra("thumbnail",data.courseThumbnail)
            intent.putExtra("courseId",data.courseId)
            context.startActivity(intent)
        }
    }
}
