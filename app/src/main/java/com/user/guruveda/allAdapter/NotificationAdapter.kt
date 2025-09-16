package com.user.guruveda.allAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.user.guruveda.Notification.NotificationContent
import com.user.guruveda.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationAdapter(val notificationList: MutableList<NotificationContent>) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = notificationList[position]
        holder.itemView.findViewById<TextView>(R.id.tvNotificationTitle).text = notification.title
        holder.itemView.findViewById<TextView>(R.id.tvNotificationBody).text = notification.body
        holder.itemView.findViewById<TextView>(R.id.tvNotificationTimestamp).text = formatTimestamp(notification.timestamp)

        // Load a default image
        Glide.with(holder.itemView.context)
            .load(R.drawable.logo2)
            .into(holder.itemView.findViewById<ImageView>(R.id.ivNotificationImage))
    }

    override fun getItemCount(): Int = notificationList.size

    private fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    fun removeItem(position: Int) {
        notificationList.removeAt(position)
        notifyItemRemoved(position)
    }
}