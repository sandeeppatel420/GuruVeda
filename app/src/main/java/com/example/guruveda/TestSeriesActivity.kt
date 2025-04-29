package com.example.guruveda

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guruveda.DataModel.TestSeriesDataModal
import com.example.guruveda.ViewModel.TestSeriesViewModel
import com.example.guruveda.allAdapter.TestSeriesAdapter

class TestSeriesActivity : AppCompatActivity() {
    private lateinit var myRecyclerView: RecyclerView
    private lateinit var adapter: TestSeriesAdapter
    private val testSeriesList = ArrayList<TestSeriesDataModal>()
    private lateinit var testSeriesViewModel: TestSeriesViewModel
    private lateinit var arrowBack: ImageView

    @SuppressLint("NotifyDataSetChanged", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_series)

        myRecyclerView = findViewById(R.id.recyclerView)
        arrowBack = findViewById(R.id.arrowBack)
        arrowBack.setOnClickListener {
            onBackPressed()
        }
        myRecyclerView.layoutManager =
            GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false)
        adapter = TestSeriesAdapter(testSeriesList)
        myRecyclerView.adapter = adapter
        testSeriesViewModel = ViewModelProvider(this).get(TestSeriesViewModel::class.java)
        testSeriesViewModel.fetchTestSeriesData()
        testSeriesViewModel.testSeriesList.observe(this) {
            testSeriesList.clear()
            testSeriesList.addAll(it)
            adapter.notifyDataSetChanged()
        }


        setStatusBar()
    }

    private fun setStatusBar() {
        val statusBarColor = ContextCompat.getColor(this, R.color.black2)
        window.statusBarColor = statusBarColor
    }
}