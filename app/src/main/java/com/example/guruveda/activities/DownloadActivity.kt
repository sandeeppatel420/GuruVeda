package com.example.guruveda.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guruveda.DataModel.VideoModel
import com.example.guruveda.R
import com.example.guruveda.allAdapter.DownloadedVideoAdapter
import com.google.android.material.appbar.MaterialToolbar
import org.json.JSONObject

class DownloadActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DownloadedVideoAdapter
    private lateinit var downloadedVideosList: ArrayList<VideoModel>
    lateinit var downloadVideosTopAppBar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        recyclerView = findViewById(R.id.downloadedRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        downloadedVideosList = ArrayList()
        adapter = DownloadedVideoAdapter(this, downloadedVideosList)
        recyclerView.adapter = adapter
        downloadVideosTopAppBar=findViewById<MaterialToolbar>(R.id.downloadVideosTopAppBar)
        downloadVideosTopAppBar.setNavigationOnClickListener {
            onBackPressed()

        }

        val prefs = getSharedPreferences("downloads", MODE_PRIVATE)
        val allEntries = prefs.all

        for ((_, value) in allEntries) {
            val stringValue = value as? String ?: continue

            try {
                val json = JSONObject(stringValue)
                val video = VideoModel(
                    title = json.optString("title", ""),
                    videoUrl = json.optString("path", ""),
                    duration = json.optString("duration", ""),
                    type = json.optString("type", "")
                )
                downloadedVideosList.add(video)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        adapter.notifyDataSetChanged()
    }
}
