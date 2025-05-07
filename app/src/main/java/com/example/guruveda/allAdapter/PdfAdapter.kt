package com.example.guruveda.allAdapter

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.guruveda.DataModel.PdfModel
import com.example.guruveda.activities.PdfViewActivity
import com.example.guruveda.R

class PdfAdapter(private val myContext: Context, private val pdfList:ArrayList<PdfModel>):RecyclerView.Adapter<PdfAdapter.PdfViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {
       val myView=LayoutInflater.from(myContext).inflate(R.layout.pdf_item,parent,false)
        return PdfViewHolder(myView)
    }

    fun updateList(newList: List<PdfModel>) {
        pdfList.clear()
        pdfList.addAll(newList)
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        val users= pdfList[position]
        holder.pdfTitle.text=users.pdfTitle
        holder.pdfImage.setImageResource(R.drawable.pdf2)
        holder.itemView.setOnClickListener {
            val intent=Intent(myContext, PdfViewActivity::class.java)
            intent.putExtra("title",users.pdfTitle)
            intent.putExtra("pdfUrl",users.pdfUrl)
            myContext.startActivity(intent)
        }
        holder.pdfDotImage.setOnClickListener {
           showPopupMenu(myContext,it,users.pdfUrl.toString(),users.pdfTitle.toString())
        }
    }

    override fun getItemCount(): Int {
      return pdfList.size
    }

    class PdfViewHolder(itemView: View):ViewHolder(itemView){
      val pdfImage=itemView.findViewById<ImageView>(R.id.pdf_image)
      val pdfTitle=itemView.findViewById<TextView>(R.id.pdf_title)
      val pdfDotImage=itemView.findViewById<ImageView>(R.id.pdf_dot_imageView)
    }
    private fun showPopupMenu(ucontext: Context, anchorView: View,mYuri:String,myfileName: String) {
        val popup = PopupMenu(ucontext, anchorView)
        popup.menuInflater.inflate(R.menu.pop_menu, popup.menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_download -> {

                    downloadPdf(ucontext,mYuri,myfileName)
                    true
                }

                else -> false
            }
        }
        popup.show()
    }

    private fun downloadPdf(context: Context, url: String, fileName: String) {
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle(fileName)
            .setDescription("Downloading PDF...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$fileName.pdf")
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }
}