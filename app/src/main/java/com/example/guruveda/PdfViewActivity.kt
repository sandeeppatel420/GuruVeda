package com.example.guruveda

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.barteksc.pdfviewer.PDFView
import com.google.firebase.storage.FirebaseStorage

class PdfViewActivity : AppCompatActivity() {
    private lateinit var pdfView: PDFView
    private lateinit var progressBar: ProgressBar
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_view)

        val backImage=findViewById<ImageView>(R.id.pdfView_back)
        backImage.setOnClickListener {
            finish()
        }
        val pdfTitle=intent.getStringExtra("title")
        val pdfImage=intent.getStringExtra("pdfUrl")

        val viewTitle=findViewById<TextView>(R.id.pdfView_title)
        viewTitle.text=pdfTitle

        pdfView=findViewById(R.id.pdfView)
        progressBar = findViewById(R.id.progress_bar_pdfView)

        if (pdfImage != null) {
            loadPdfFromFirebase(pdfImage, pdfView)
        }

    setStatusBar()
    }

    private fun setStatusBar() {
        val statusBarColor = ContextCompat.getColor(this, R.color.black2)
        window.statusBarColor = statusBarColor
    }

    private fun loadPdfFromFirebase(pdfUrl: String, pdfView: PDFView) {
        progressBar.visibility=View.VISIBLE
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl)
        storageReference.getBytes(Long.MAX_VALUE).addOnSuccessListener { bytes ->
            pdfView.fromBytes(bytes)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .enableAntialiasing(true)
                .onLoad {
                    progressBar.visibility = View.GONE
                }
                .load()
        }.addOnFailureListener {
            progressBar.visibility = View.GONE
            Toast.makeText(this, "Failed to load PDF", Toast.LENGTH_SHORT).show()
        }
    }
}