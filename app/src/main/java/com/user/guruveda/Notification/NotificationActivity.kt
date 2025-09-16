package com.user.guruveda.Notification

import android.Manifest
import android.app.ComponentCaller
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.user.guruveda.allAdapter.NotificationAdapter
import com.user.guruveda.databinding.ActivityNotificationBinding
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding
    private lateinit var notificationAdapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up toolbar
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        // Set up RecyclerView
        setupRecyclerView()

        // Handle notification from intent
        handleNotificationIntent(intent)

        // Get FCM token
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("FCM_TOKEN", it.result)
            } else {
                Log.e("FCM_TOKEN", "Failed to get token: ${it.exception?.message}")
            }
        }

        // Get server key & send notification
        ServerKey(this).getAccessToken { token ->
            if (token != null) {
                Log.d("AccessToken", ": $token")
                val editor = getSharedPreferences("token", MODE_PRIVATE).edit()
                editor.putString("token", token)
                editor.apply()

                sendNotificationToUser(token)
            } else {
                Log.e("AccessToken", "Failed to get access token")
                Toast.makeText(this, "Failed to get access token", Toast.LENGTH_SHORT).show()
            }
        }

        // Permission for notification (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1001)
        }
    }
   override fun onNewIntent(intent: Intent, caller: ComponentCaller) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleNotificationIntent(intent)
    }

    private fun handleNotificationIntent(intent: android.content.Intent?) {
        intent?.let {
            val title = it.getStringExtra("notification_title")
            val body = it.getStringExtra("notification_body")
            val timestamp = it.getLongExtra("notification_timestamp", 0L)

            if (title != null && body != null) {
                val notification = NotificationContent(
                    title = title,
                    body = body,
                    timestamp = timestamp
                )
                NotificationRepository.addNotification(notification)

                notificationAdapter.notificationList.clear()
                notificationAdapter.notificationList.addAll(NotificationRepository.getNotifications())
                notificationAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setupRecyclerView() {
        notificationAdapter = NotificationAdapter(NotificationRepository.getNotifications().toMutableList())
        binding.notificationRecycler.apply {
            layoutManager = LinearLayoutManager(this@NotificationActivity)
            adapter = notificationAdapter
        }

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val notification = notificationAdapter.notificationList[position]
                NotificationRepository.removeNotification(notification.id)
                notificationAdapter.removeItem(position)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.notificationRecycler)
    }

    private fun sendNotificationToUser(serverKey: String) {
        val targetToken = "c4b0gfZ1SQek3PGKHL9K24:APA91bH46e2j537mNCA43ntuX19PDpDK6wsM3ba_NCNcfGzfvtt6FNusG5hKxhdazT1ITgrgO6ROR5rYX06nFs4L1UraHwC_3S8roOJB6rTCJgzhu-HAvTM"

        val notificationMessage = NotificationModel(
            message = MessageContent(
                token = targetToken,
                notification = NotificationContent(
                    title = "Welcome to Guruveda",
                    body = "This is a message from NotificationActivity.",
                    timestamp = System.currentTimeMillis()
                )
            )
        )

        SeverObject.retrofit.sendNotification("Bearer $serverKey", notificationMessage)
            .enqueue(object : retrofit2.Callback<NotificationModel> {
                override fun onResponse(
                    call: Call<NotificationModel>,
                    response: retrofit2.Response<NotificationModel>
                ) {
                    if (!response.isSuccessful) {
                        Log.e("SendNotification", "Failed: ${response.errorBody()?.string()}")
                        Toast.makeText(this@NotificationActivity, "Failed to Send Notification", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<NotificationModel>, t: Throwable) {
                    Log.e("SendNotification", "Error: ${t.message}")
                    Toast.makeText(this@NotificationActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
