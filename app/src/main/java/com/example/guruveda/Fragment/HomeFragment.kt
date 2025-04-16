package com.example.guruveda.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.guruveda.AllCoursesActivity
import com.example.guruveda.R
import com.example.guruveda.SelectedActivity
import com.example.guruveda.ViewModel.GetUserDataViewModel


class HomeFragment : Fragment() {
    private lateinit var profileIcon:ImageView
    private lateinit var profileViewModel:GetUserDataViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.fragment_home, container, false)
        val textView1 = view.findViewById<TextView>(R.id.allCouresHome)
        textView1.setOnClickListener {
            val intent = Intent(requireContext(), AllCoursesActivity::class.java)
            startActivity(intent)

        }






        profileViewModel=ViewModelProvider(this)[GetUserDataViewModel::class.java]
        profileIcon=view.findViewById(R.id.profile_icon_image)
        profileViewModel.getUser()
        profileViewModel.users1.observe(viewLifecycleOwner) {
            Glide.with(requireContext())
                .load(it.imageProfile)
                .into(profileIcon)
        }


        val imageSlider = view.findViewById<ImageSlider>(R.id.image_slider)
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.sildeimg_2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.sildeimg_2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.sildeimg_2, ScaleTypes.FIT))
        imageSlider.setImageList(imageList)


        val selectedText=view.findViewById<TextView>(R.id.selected_TextView)
        selectedText.setOnClickListener {
         startActivity(Intent(requireContext(),SelectedActivity::class.java))
        }
        return view
    }


}