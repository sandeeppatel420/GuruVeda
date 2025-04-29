package com.example.guruveda.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guruveda.DataModel.PdfModel
import com.example.guruveda.R
import com.example.guruveda.ViewModel.PdfViewModel
import com.example.guruveda.allAdapter.PdfAdapter

class PdfFragment : Fragment() {
    private lateinit var myPdfAdapter: PdfAdapter
    private lateinit var pdfList:ArrayList<PdfModel>
    private lateinit var myPdfRecyclerView: RecyclerView
    private lateinit var pdfViewModel: PdfViewModel
    private lateinit var fullPdfList: ArrayList<PdfModel>

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val myView=LayoutInflater.from(requireContext()).inflate(R.layout.fragment_library, container, false)

        myPdfRecyclerView=myView.findViewById(R.id.pdf_recyclerView)
        myPdfRecyclerView.layoutManager=LinearLayoutManager(requireContext())

        pdfList= ArrayList()

        myPdfAdapter= PdfAdapter(requireContext(),pdfList)
        myPdfRecyclerView.adapter=myPdfAdapter

        pdfViewModel = ViewModelProvider(this)[PdfViewModel::class.java]

        pdfViewModel.pdfList.observe(viewLifecycleOwner) { list ->
            pdfList.clear()
            pdfList.addAll(list)
            fullPdfList=ArrayList(list)
            myPdfAdapter.notifyDataSetChanged()
        }


        val progressBar = myView.findViewById<ProgressBar>(R.id.progress_bar)
        pdfViewModel.isLoading.observe(viewLifecycleOwner){  loading ->
           if(loading) {
               progressBar.visibility=View.VISIBLE
           }
            else{
               progressBar.visibility=View.GONE
           }
        }

        pdfViewModel.errorMessage.observe(viewLifecycleOwner){ errorMsg->
            Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
        }
        pdfViewModel.fetchPdfData()




        val searchEditText=myView.findViewById<EditText>(R.id.search_EditText)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().lowercase().trim()

                val filteredList = fullPdfList.filter {
                    it.pdfTitle?.lowercase()?.contains(query) == true
                }

                myPdfAdapter.updateList(filteredList)
            }
        })
        return myView
    }




}