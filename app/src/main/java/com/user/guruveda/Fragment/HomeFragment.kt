package com.user.guruveda.Fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.user.guruveda.DataModel.CourseModel
import com.user.guruveda.DataModel.FreeVideosDataModel
import com.user.guruveda.Notification.NotificationActivity
import com.user.guruveda.activities.DoubtActivity

import com.user.guruveda.R
import com.user.guruveda.TestSeriesActivity
import com.user.guruveda.activities.SelectCoursesActivity
import com.user.guruveda.ViewModel.GetUserDataViewModel
import com.user.guruveda.activities.AllCourseActivity
import com.user.guruveda.activities.DownloadActivity
import com.user.guruveda.activities.FreeVideoActivity
import com.user.guruveda.allAdapter.FreeVideosAdapter
import com.user.guruveda.allAdapter.RecommendedCoursesAdapter
import com.google.firebase.firestore.FirebaseFirestore


class HomeFragment : Fragment() {
    private lateinit var profileIcon: ImageView
    private lateinit var profileViewModel: GetUserDataViewModel
    lateinit var db: FirebaseFirestore
    lateinit var freeVideosList: ArrayList<FreeVideosDataModel>
    lateinit var recommendedCoursesList: ArrayList<CourseModel>
    lateinit var recommendedCoursesRecyclerView: RecyclerView
    lateinit var freeVideosRecyclerView: RecyclerView
    lateinit var recommendedCoursesAdapter: RecommendedCoursesAdapter
    lateinit var freeVideosAdapter: FreeVideosAdapter
    lateinit var testSeries: TextView
    lateinit var downloads: TextView
    lateinit var viewAllRecommended: TextView


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val notificationIcon = view.findViewById<ImageView>(R.id.notification_icon)
        notificationIcon.setOnClickListener {
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
        }

        viewAllRecommended = view.findViewById(R.id.viewAllRecommended)

        viewAllRecommended.setOnClickListener {
            val intent = Intent(requireContext(), AllCourseActivity::class.java)
            startActivity(intent)
        }
        val freeVideoAll = view.findViewById<TextView>(R.id.viewAllFree)
        freeVideoAll.setOnClickListener {
            val intent = Intent(requireContext(), FreeVideoActivity::class.java)
            startActivity(intent)
        }

        db = FirebaseFirestore.getInstance()
        freeVideosList = ArrayList()
        recommendedCoursesList = ArrayList()
        recommendedCoursesAdapter = RecommendedCoursesAdapter(recommendedCoursesList)
        freeVideosAdapter = FreeVideosAdapter(freeVideosList)
        recommendedCoursesRecyclerView = view.findViewById(R.id.recommendedRecyclerView)
        freeVideosRecyclerView = view.findViewById(R.id.freeVideosRecyclerView)

        recommendedCoursesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        freeVideosRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        recommendedCoursesRecyclerView.adapter = recommendedCoursesAdapter
        freeVideosRecyclerView.adapter = freeVideosAdapter

        val textView1 = view.findViewById<TextView>(R.id.allCouresHome)
        val selectedTextView = view.findViewById<TextView>(R.id.selected_TextView)

       val title = arguments?.getString("title")

        selectedTextView.setOnClickListener {
            val intent = Intent(requireContext(), SelectCoursesActivity::class.java)
            startActivity(intent)
        }

        textView1.setOnClickListener {
            val intent = Intent(requireContext(), AllCourseActivity::class.java)
            startActivity(intent)

        }

        val freeVideo = view.findViewById<TextView>(R.id.freeVideoHome)
        freeVideo.setOnClickListener {
            val intent = Intent(requireContext(), FreeVideoActivity::class.java)
            startActivity(intent)
        }

        val doubts = view.findViewById<TextView>(R.id.doubtHome)
        doubts.setOnClickListener {
            val intent = Intent(requireContext(), DoubtActivity::class.java)
            startActivity(intent)
        }
        testSeries=view.findViewById(R.id.testSeriesHome)
        testSeries.setOnClickListener {
            val intent = Intent(requireContext(), TestSeriesActivity::class.java)
            startActivity(intent)
        }
        downloads=view.findViewById(R.id.downloadHome)
        downloads.setOnClickListener {
            val intent = Intent(requireContext(), DownloadActivity::class.java)
            startActivity(intent)
        }


        profileViewModel = ViewModelProvider(this)[GetUserDataViewModel::class.java]
        profileIcon = view.findViewById(R.id.profile_icon_image)
        profileViewModel.getUser()
        profileViewModel.users1.observe(viewLifecycleOwner) {
            Glide.with(requireContext())
                .load(it.imageProfile)
                .into(profileIcon)
        }

        val selectedCourse = arguments?.getString("courses")
        val freeVideo1 = arguments?.getString("freeVideo")

        val imageSlider = view.findViewById<ImageSlider>(R.id.image_slider)
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.sildeimg_2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.sildeimg_2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.sildeimg_2, ScaleTypes.FIT))
        imageSlider.setImageList(imageList)

        if (selectedCourse != null) {
            selectedCoursesGet(selectedCourse)
        } else {
            coursesGet()
        }

        if (freeVideo1 != null) {
            freeVideosGet(freeVideo1)
        }
        else{
            freeVideosGet1()
        }

        return view
    }

    fun selectedCoursesGet(courses: String) {

        db.collection("courses").whereEqualTo("courseTitle", courses).get()
            .addOnSuccessListener {
                for (document in it.documents) {
                    val course = document.toObject(CourseModel::class.java)
                    recommendedCoursesList.add(course!!)

                }

            }

    }

    fun coursesGet(){
        db.collection("courses").get()
            .addOnSuccessListener {
                for (document in it.documents) {
                    val course = document.toObject(CourseModel::class.java)
                    recommendedCoursesList.add(course!!)

                }
                recommendedCoursesAdapter.notifyDataSetChanged()

            }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun freeVideosGet(type: String) {
        db.collection("videos").whereEqualTo("freeVideo", type).get()
            .addOnSuccessListener {
                freeVideosList.clear()

                for (document in it.documents) {
                    val freeVideo = document.toObject(FreeVideosDataModel::class.java)
                    freeVideosList.add(freeVideo!!)

                }
                freeVideosAdapter.notifyDataSetChanged()
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun freeVideosGet1(){
        db.collection("videos").get()
            .addOnSuccessListener {
                freeVideosList.clear()

                for (document in it.documents) {
                    val freeVideo = document.toObject(FreeVideosDataModel::class.java)
                    freeVideosList.add(freeVideo!!)

                }
                freeVideosAdapter.notifyDataSetChanged()
            }
    }


}