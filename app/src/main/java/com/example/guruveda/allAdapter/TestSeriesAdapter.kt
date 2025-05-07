package com.example.guruveda.allAdapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.guruveda.DataModel.TestSeriesDataModal
import com.example.guruveda.R
import com.example.guruveda.AllTestActivity.TestSeriesDetailsActivity

class TestSeriesAdapter(private var testSeriesList: ArrayList<TestSeriesDataModal>): RecyclerView.Adapter<TestSeriesAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val testSubjectTextView = itemView.findViewById<TextView>(R.id.test_subject)
        val testTittleTextView = itemView.findViewById<TextView>(R.id.test_tittle)
        val testDescriptionTextView = itemView.findViewById<TextView>(R.id.test_description)
        val testDurationTextView = itemView.findViewById<TextView>(R.id.test_duration)
        val texstImageView = itemView.findViewById<ImageView>(R.id.test_image)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestSeriesAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.test_series_layout, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: TestSeriesAdapter.ViewHolder, position: Int) {
        val testSeries = testSeriesList[position]
        holder.testSubjectTextView.text = testSeries.testSubject
        holder.testTittleTextView.text = testSeries.testTitle
        holder.testDescriptionTextView.text = testSeries.testDescription
        holder.testDurationTextView.text = testSeries.timeDuration
        Glide.with(holder.itemView.context).load(testSeries.image).into(holder.texstImageView)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, TestSeriesDetailsActivity::class.java)
            intent.putExtra("subject", testSeries.testSubject)
            intent.putExtra("title", testSeries.testTitle)
            intent.putExtra("description", testSeries.testDescription)
            intent.putExtra("duration", testSeries.timeDuration)
            intent.putExtra("imageUrl", testSeries.image)
            holder.itemView.context.startActivity(intent)
        }




    }


    override fun getItemCount(): Int {
        return testSeriesList.size

    }


}