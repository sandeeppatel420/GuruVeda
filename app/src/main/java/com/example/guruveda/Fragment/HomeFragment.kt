package com.example.guruveda.Fragment

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
import com.example.guruveda.AllCoursesActivity
import com.example.guruveda.DataModel.CourseModel
import com.example.guruveda.DataModel.FreeVideosDataModel
import com.example.guruveda.DoubtActivity
import com.example.guruveda.FreeVideoActivity
import com.example.guruveda.R
import com.example.guruveda.View.SelectCoursesActivity
import com.example.guruveda.ViewModel.GetUserDataViewModel
import com.example.guruveda.allAdapter.FreeVideosAdapter
import com.example.guruveda.allAdapter.RecommendedCoursesAdapter
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)


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


        selectedTextView.setOnClickListener {
            val intent = Intent(requireContext(), SelectCoursesActivity::class.java)
            startActivity(intent)
        }

        textView1.setOnClickListener {
            val intent = Intent(requireContext(), AllCoursesActivity::class.java)
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