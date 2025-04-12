package com.example.guruveda.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.guruveda.AllCoursesActivity
import com.example.guruveda.R


class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.fragment_home, container, false)
        val textView = view.findViewById<TextView>(R.id.allCouresHome)
        textView.setOnClickListener {
            val intent = Intent(requireContext(), AllCoursesActivity::class.java)
            startActivity(intent)
        }
        return view
    }


}