package com.example.guruveda.activities
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.guruveda.DataModel.VideoModel
import com.example.guruveda.ViewModel.DownloadViewModel
import com.example.guruveda.ViewModel.VideoGetViewModel
import com.example.guruveda.allAdapter.VideoAdapter
import com.example.guruveda.databinding.ActivityCourseDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject
import kotlin.collections.hashMapOf

class CourseDetailActivity : AppCompatActivity(),PaymentResultListener {
    private lateinit var binding: ActivityCourseDetailBinding
    private lateinit var adapter: VideoAdapter
    private lateinit var list: ArrayList<VideoModel>
    private lateinit var viewModel: VideoGetViewModel
    private lateinit var downloadViewModel: DownloadViewModel
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val description = intent.getStringExtra("description")
        val title = intent.getStringExtra("title")
        val price = intent.getStringExtra("price")
        val videoCount = intent.getStringExtra("videoCount")
        val thumbnail = intent.getStringExtra("thumbnail")



        binding.descriptionDetail.text = description
        binding.titleDetail.text = title
        binding.priceDetail.text = price
        binding.videoCountText.text = videoCount
        Glide.with(this).load(thumbnail).into(binding.courseImageDetail)

        list = ArrayList()
        downloadViewModel = ViewModelProvider(this)[DownloadViewModel::class.java]
        adapter = VideoAdapter(this, list ,downloadViewModel)
        binding.videoRecyclerView.adapter = adapter
        binding.videoRecyclerView.layoutManager = LinearLayoutManager(this)

        viewModel = ViewModelProvider(this)[VideoGetViewModel::class.java]
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.courseProgressBar.visibility = View.VISIBLE
            } else {
                binding.courseProgressBar.visibility = View.GONE
            }
        }
        viewModel.errorMessage.observe(this) { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()


        }
        viewModel.videoLiveData.observe(this) {
            list.clear()
            list.addAll(it!!)
            adapter.notifyDataSetChanged()
        }
        val courseId = intent.getStringExtra("courseId")

        if (courseId != null) {
            viewModel.getVideos(courseId)
        }
        binding.payButton.setOnClickListener {
            payment()
        }
        Checkout.preload(applicationContext)

    }
   private fun payment(){
      val checkout = Checkout()
       checkout.setKeyID("rzp_test_7QRDUFvP75uLJS")
       try {
           val options = JSONObject()
           options.put("name","Guruveda")
           options.put("description","Test payment")
           options.put("theme.color","#3399cc")
           options.put("currency","INR")
           options.put("amount","10000")
            val prefill = JSONObject()
           prefill.put("email","test@example.com")
           prefill.put("contact","9191919191")
           options.put("prefill",prefill)
           checkout.open(this,options)
       } catch (e: Exception) {
           Toast.makeText(this, "Error in payment: ${e.message}", Toast.LENGTH_SHORT).show()
       }
    }
    override fun onPaymentSuccess(razorpayPaymentID: String?) {
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val courseId = intent.getStringExtra("courseId")
         if (userId != null && courseId != null) {
             val db = FirebaseFirestore.getInstance()
             val data = hashMapOf(
                 "courseId" to courseId,
                 "paymentId" to razorpayPaymentID,
                 "timestamp" to System.currentTimeMillis()

             )
             db.collection("users").document(userId).collection("myCourses").document(courseId).set(data)
                 .addOnSuccessListener {
                     Toast.makeText(this, "Course added to My Courses", Toast.LENGTH_SHORT).show()
                     val intent = Intent(this, MainActivity::class.java)
                     intent.putExtra("showMyCourses", true)
                     startActivity(intent)
                 }
                 .addOnFailureListener {
                     Toast.makeText(this, "Failed to add course to My Courses", Toast.LENGTH_SHORT)
                         .show()
                 }
         }


    }
    override fun onPaymentError(code: Int, response: String?) {
        Toast.makeText(this, "Payment Failed: $response", Toast.LENGTH_SHORT).show()
    }
}