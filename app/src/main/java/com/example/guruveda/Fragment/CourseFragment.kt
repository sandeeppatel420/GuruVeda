package com.example.guruveda.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.guruveda.MainActivity
import com.example.guruveda.R

class CourseFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val myView=LayoutInflater.from(requireContext()).inflate(R.layout.fragment_course, container, false)

        val clickedBtn = myView.findViewById<Button>(R.id.addBtn1)
        clickedBtn.setOnClickListener {
            (activity as? MainActivity)?.selectFragment(R.id.home_icon, HomeFragment())
            Toast.makeText(requireContext(), "clicked", Toast.LENGTH_SHORT).show()
        }
        return myView
    }


}