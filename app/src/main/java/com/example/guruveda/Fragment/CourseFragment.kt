package com.example.guruveda.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guruveda.DataModel.CourseModel
import com.example.guruveda.R
import com.example.guruveda.ViewModel.AllCouresGet
import com.example.guruveda.allAdapter.AdapterCoures
import com.google.firebase.auth.FirebaseAuth

class CourseFragment : Fragment() {
      lateinit var viewModel: AllCouresGet
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterCoures
    private var courseList = ArrayList<CourseModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val myView = LayoutInflater.from(requireContext())
            .inflate(R.layout.fragment_course, container, false)

        recyclerView = myView.findViewById(R.id.myCoursesRecyclerView)
        adapter = AdapterCoures(requireContext(),courseList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)

viewModel = ViewModelProvider(this)[AllCouresGet::class.java]
        viewModel.myCoursesLiveData.observe (viewLifecycleOwner){
            courseList.clear()
            courseList.addAll(it)
            adapter.notifyDataSetChanged()
        }

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            viewModel.getMyCourses(userId)
        }
            return myView

    }

}