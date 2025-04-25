package com.example.guruveda.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guruveda.DataModel.PdfModel
import com.example.guruveda.R
import com.example.guruveda.allAdapter.PdfAdapter

class PdfFragment : Fragment() {
    private lateinit var myPdfAdapter: PdfAdapter
    private lateinit var pdfList:ArrayList<PdfModel>
    private lateinit var myPdfRecyclerView: RecyclerView
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val myView=LayoutInflater.from(requireContext()).inflate(R.layout.fragment_library, container, false)

        myPdfRecyclerView=myView.findViewById(R.id.pdf_recyclerView)
        myPdfRecyclerView.layoutManager=LinearLayoutManager(requireContext())

        pdfList= ArrayList()
        pdfList.add(PdfModel("","Kotlin"))
        pdfList.add(PdfModel("","Java"))
        pdfList.add(PdfModel("","Dart"))
        pdfList.add(PdfModel("","C++"))

        myPdfAdapter= PdfAdapter(requireContext(),pdfList)
        myPdfRecyclerView.adapter=myPdfAdapter

        return myView
    }


}