package com.example.guruveda.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.guruveda.R

class PdfFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val myView=LayoutInflater.from(requireContext()).inflate(R.layout.fragment_library, container, false)


        return myView
    }


}