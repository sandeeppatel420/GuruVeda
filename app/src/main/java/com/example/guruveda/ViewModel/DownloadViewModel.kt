package com.example.guruveda.ViewModel

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guruveda.DataModel.VideoModel
import com.example.guruveda.activities.DownloadActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

class DownloadViewModel : ViewModel() {
    fun downloadVideo(context: Context, data: VideoModel, onComplete: (Boolean, String) -> Unit) {
        val videoUrl = data.videoUrl
        var title = data.title ?: "video_${System.currentTimeMillis()}"
        title = title.replace(Regex("[^a-zA-Z0-9_.]"), "_")

        if (videoUrl != null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val client = OkHttpClient()
                    val request = Request.Builder().url(videoUrl).build()
                    val response = client.newCall(request).execute()

                    if (response.isSuccessful) {
                        val videoBytes = response.body?.bytes()
                        if (videoBytes != null) {
                            val file = File(context.filesDir, "$title.mp4")
                            val fos = FileOutputStream(file)
                            fos.write(videoBytes)
                            fos.close()

                            val prefs = context.getSharedPreferences("downloads", Context.MODE_PRIVATE)
                            val editor = prefs.edit()

                            val videoJson = JSONObject()
                            videoJson.put("title", data.title)
                            videoJson.put("duration", data.duration)
                            videoJson.put("type", data.type)
                            videoJson.put("path", file.absolutePath)

                            editor.putString(title, videoJson.toString())
                            editor.apply()

                            withContext(Dispatchers.Main) {
                                onComplete(true, "Download completed")
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                onComplete(false, "Empty file")
                            }
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            onComplete(false, "Server error")
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        onComplete(false, "Download failed")
                    }
                }
            }
        } else {
            onComplete(false, "Video URL not found")
        }
    }

}
