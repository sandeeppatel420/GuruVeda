package com.example.guruveda.Fragment

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.guruveda.Auth.LoginActivity
import com.example.guruveda.Auth.ProfileEditActivity
import com.example.guruveda.DataModel.AuthModel
import com.example.guruveda.MainActivity
import com.example.guruveda.R
import com.example.guruveda.ViewModel.GetUserDataViewModel
import com.example.guruveda.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {
    private var binding: FragmentProfileBinding?=null
    private lateinit var viewModel: GetUserDataViewModel
    private lateinit var datalist:ArrayList<AuthModel>
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentProfileBinding.inflate(inflater, container, false)
        val view=binding?.root
        binding?.userLogoutText?.setOnClickListener {
            logout()
        }


        datalist=ArrayList()
        binding?.editLayout?.setOnClickListener {
            val name=binding?.userNameText1?.text.toString()
            val email=binding?.userEmailText1?.text.toString()
          val intent=Intent(requireContext(),ProfileEditActivity::class.java)
            intent.putExtra("name",name)
            intent.putExtra("email",email)
            intent.putExtra("imageProfile",datalist[0].imageProfile)
            intent.putExtra("id",datalist[0].userId)
            startActivity(intent)
        }

        viewModel= ViewModelProvider(this)[GetUserDataViewModel::class.java]
        viewModel.getUser()
        viewModel.users1.observe(viewLifecycleOwner){
            binding?.userNameText1?.text=it.name
            binding?.userEmailText1?.text=it.email
            binding?.userPhoneText1?.text=it.phoneNumber
            datalist.add(it)
            Glide.with(requireContext())
                .load(it.imageProfile)
                .into(binding?.profileImage!!)
            binding?.progressCircular1?.visibility = View.GONE
        }


        binding?.progressCircular1?.visibility = View.VISIBLE
        binding?.progressCircular1?.indeterminateTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Red))

        binding?.backspace?.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout_container, HomeFragment())
                .addToBackStack(null)
                .commit()
            (activity as? MainActivity)?.selectFragment(R.id.home_icon, HomeFragment())
        }
        return view
    }

    private fun logout(){
        val dialog= AlertDialog.Builder(requireContext())
        dialog.setTitle("Log Out")
        dialog.setMessage("Are you sure you want to logout?")
        dialog.setPositiveButton("Logout"){ b ,_->
            FirebaseAuth.getInstance().signOut()
            val intent= Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
            b.dismiss()
            Toast.makeText(requireContext(), "Logout", Toast.LENGTH_SHORT).show()
        }
        dialog.setNegativeButton("Cancle"){a,_->
            a.cancel()
            Toast.makeText(requireContext(), "Cancle", Toast.LENGTH_SHORT).show()
        }
        dialog.setCancelable(false)
        val alertDialog = dialog.create()

        alertDialog.show()


        val positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
        positiveButton.setTextColor(
            ContextCompat.getColor(requireContext(),
            R.color.black2
        ))

        val negativeButton=alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        negativeButton.setTextColor(
            ContextCompat.getColor(requireContext(),
            R.color.black2
        ))
    }
}