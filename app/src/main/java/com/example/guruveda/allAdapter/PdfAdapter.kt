package com.example.guruveda.allAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.guruveda.DataModel.PdfModel
import com.example.guruveda.R

class PdfAdapter(private val myContext: Context, private val pdfList:ArrayList<PdfModel>):RecyclerView.Adapter<PdfAdapter.PdfViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {
       val myView=LayoutInflater.from(myContext).inflate(R.layout.pdf_item,parent,false)
        return PdfViewHolder(myView)
    }

    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        val users= pdfList[position]
        holder.pdfTitle.text=users.pdfTitle
    }

    override fun getItemCount(): Int {
      return pdfList.size
    }
    class PdfViewHolder(itemView: View):ViewHolder(itemView){
      val pdfImage=itemView.findViewById<ImageView>(R.id.pdf_image)
      val pdfTitle=itemView.findViewById<TextView>(R.id.pdf_title)
    }
}